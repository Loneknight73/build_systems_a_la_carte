package bsalc

import cats.data.State
import cats.data.State.{get, inspect, modify, set}
import cats.data.Const
import cats.{Monoid, Applicative, Monad}
import cats.mtl.Stateful
import cats.implicits.*
import cats.syntax.all.*

import com.flowtick.graphs.*
import com.flowtick.graphs.algorithm.*
import com.flowtick.graphs.defaults.*
import com.flowtick.graphs.defaults.anyId.identifyAny
import com.flowtick.graphs.cat.instances._

import bsalc.Store.{getInfo, putInfo}

type DGraph[K] = Graph[Unit, K]

object Make {

  import cats.Applicative
  import cats.mtl.Stateful
  import cats.implicits._
  import Store.{getValue, putValue}

  type Time = Int
  type MakeInfo[K] = (Time, Map[K, Time])

  // returns True if at least one item in the list fulfills the condition
  def any[A](p: A => Boolean, l: List[A]): Boolean = {
    l.foldLeft(false)((acc, elem) => acc || p(elem))
  }

  // TODO: this duplicates the function in object TestDependencies
  def dependencies[K, V](task: Task[Applicative, K, V]): List[K] = {
    task.run(k => Const(List(k))).getConst
  }

  def modTimeRebuilder[K: Ordering, V](): Rebuilder[Applicative, MakeInfo[K], K, V] = {
    (key: K, value: V, task: Task[Applicative, K, V]) => {
      type MonadStateMakeInfo[A[_]] = Stateful[A, MakeInfo[K]]
      new Task[MonadStateMakeInfo, K, V] {
        /*
        The definition of run should be def run[F[_] : Monad : MonadStateMakeInfo], because
        in cats Stateful is not a Monad (MonadState in Haskell MTL is a Monad instead).
        Unfortunately this changes the signature of the function run (every context bound
        corresponds to an implicit function parameter, and run() in class Task has only
        one context bound).
        This probably means it is not possible to use monadic for syntax in this method,
        and we have to translate the code in a different way.
        Besides this it is not clear how to render:
          if not dirty then return value else do
              put (now + 1, Map.insert key now modTimes)
              run task fetch
        it should be a 'forked' for inside another for, but I am not sure if this is possible
        in Scala.
         */
        def run[F[_] : MonadStateMakeInfo](fetch: K => F[V]): F[V] = {
          val ev = summon[MonadStateMakeInfo[F]]
          val evMonad = ev.monad
          evMonad.flatMap(ev.get)((now, modTimes) =>
            val dirty = modTimes.get(key) match {
              case None => true
              case Some(time) => any((d: K) => modTimes.get(d).map(_ > time).get, dependencies(task))
            }
            if (!dirty) then
              ev.monad.pure(value)
            else {
              ev.set(now + 1, modTimes + (key -> now))
              task.run(fetch)(ev.monad)
            }
          )
        }
      }
    }
  }

  def topological[I, K: Ordering, V](): Scheduler[Applicative, I, I, K, V] = {
    (rebuilder: Rebuilder[Applicative, I, K, V]) => {
      // TODO: can these be curried?
      (tasks: Tasks[Applicative, K, V], target: K, store: Store[I, K, V]) => {
        def build(key: K): State[Store[I, K, V], Unit] = {
          tasks(key) match {
            case None => State.pure(())
            case Some(task) =>
              for {
                stor <- State.get[Store[I, K, V]]
                value = getValue(key)(stor)
                newTask: (Task[MonadStateX[I], K, V]) = rebuilder(key, value, task)
                fetch: (K => State[I, V]) = k => State.pure(getValue(k)(stor))
                newValue <- liftStore(newTask.run(fetch))
                _ <- State.modify[Store[I, K, V]](putValue(key, newValue))
              } yield ()
          }
        }

        def order: List[K] = topSort(reachable(dep, target))

        def dep: K => List[K] = {
          tasks(_) match
            case None => Nil
            case Some(task) => dependencies(task)
        }

        order.traverse(build).runS(store).value
      }
    }
  }

  def make[K: Ordering, V]: Build[Applicative, MakeInfo[K], K, V] = {
    val x: Rebuilder[Applicative, MakeInfo[K], K, V] = modTimeRebuilder[K, V]()
    val y: Scheduler[Applicative, MakeInfo[K], MakeInfo[K], K, V] = topological[MakeInfo[K], K, V]()
    y(x)
  }

  def reachable[K: Ordering : Identifiable](dep: K => List[K], target: K): DGraph[K] = {
    val depKeys = dep(target)
    val targetDepList = depKeys.map(target --> _)
    val targetDepGraph = Graph.fromEdges(targetDepList)

    val indirectDepGraphs = depKeys.map(k =>
      Graph.fromEdges(reachable(dep, k).nodes.toList.map(k --> _.value)))

    val monoid = summon[Monoid[DGraph[K]]]

    indirectDepGraphs.foldLeft(targetDepGraph)(monoid.combine(_, _))
  }

  def topSort[K: Ordering](g: DGraph[K]): List[K] = {
    g.topologicalSort.map(_.value).reverse
  }

  def liftStore[I, A, K, V](x: State[I, A]): State[Store[I, K, V], A] = {
    for {
      t <- inspect((s: Store[I, K, V]) => x.run(getInfo(s)))
      // Not clear why t is Eval[(I, A)] instead of (I, A)
      (newInfo, a) = t.value
      _ <- modify(putInfo[I, K, V](newInfo, _))
    } yield a
  }
}