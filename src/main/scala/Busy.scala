package org.bsalc

import org.bsalc.Build.Build
import org.bsalc.Store._
import org.bsalc.TasksObj.Tasks
import scalaz.Scalaz._
import scalaz.{Applicative, State}

class Busy {

  def busy[K, V]: Build[Applicative, State[Store[Unit, K, V], V], Unit, K, V] = {
    (tasks: Tasks[Applicative, State, K, V], key, store) => {
      def fetch[K, V](k: K): State[Store[Unit, K, V], V] = {
        tasks(k) match {
          case None => gets(getValue(k))
          case Some(task: Task[Applicative, State, K, V]) => for {
            v <- task.r(fetch _)
            _ <- modify(putValue(k, v))
          } yield v
        }
      }
      (fetch(key)).exec(store)
    }
  }
}
