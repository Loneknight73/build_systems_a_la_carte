package bsalc

import cats.*
import cats.data.State
import cats.data.State.{inspect, modify}
import cats.data.Const
import cats.data.WriterT
import cats.mtl.Stateful
import cats.effect.IO
import cats.effect.unsafe.implicits._
import cats.implicits.*

type MonadStateX[X] = [F[_]] =>> Stateful[F, X]

type Build[C[_[_]], I, K, V] = (Tasks[C, K, V], K, Store[I, K, V]) => Store[I, K, V]
type Scheduler[C[_[_]], I, IR, K, V] = Rebuilder[C, IR, K, V] => Build[C, I, K, V]
type Rebuilder[C[_[_]],    IR, K, V] = (K, V, Task[C, K, V]) => Task[MonadStateX[IR], K, V]

object BuildSystems {

  import Store.*

  def busy[K, V]: Build[Applicative, Unit, K, V] = { (tasks, key, store) =>
    def fetch: K => State[Store[Unit, K, V], V] = { k =>
      val x: State[Store[Unit, K, V], V] = tasks(k) match {
        case None => inspect(getValue(k))
        case Some(task) => {
          val y: State[Store[Unit, K, V], V] = for {
            v  <- task.run(fetch)
            v1 <- modify(putValue[Unit, K, V](k, v))
          } yield (v)
          y
        }
      }
      x
    }
    val z = fetch(key)
    z.runS(store).value
  }
}

object TestDependencies extends App {

    def dependencies[K, V] (task: Task[Applicative, K, V]): List[K] = {
        task.run(k => Const(List(k))).getConst
    }

    println(dependencies(Spreadsheet.sprsh1("B1").get))
    println(dependencies(Spreadsheet.sprsh1("B2").get))

}

object TestTrack extends App {
  import scala.concurrent.duration.Duration

  def track[M[_]: Monad, K, V] (task: Task[Monad, K, V], fetch: K => M[V]): M[(List[(K, V)], V)] = {
    def trackingFetch(k: K): WriterT[M, List[(K, V)], V] = {
      for {
        v <- WriterT.liftF[M, List[(K, V)], V](fetch(k))
        _ <- WriterT.tell[M, List[(K, V)]](List((k, v)))
      } yield v
    }
    task.run(trackingFetch).run
  }

  def fetchIO(k: String) = {
    for {
      - <- IO.print(s"${k}: ")
      s <- IO.readLine
    } yield s.toInt
  }

  println(track (Spreadsheet.sprsh2("B1").get, fetchIO).unsafeRunSync())
  println(track (Spreadsheet.sprsh2("B2").get, fetchIO).unsafeRunSync())
}