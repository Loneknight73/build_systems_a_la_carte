package org.bsalc
import scala.language.higherKinds

//trait Task[C[_[_]], K, V] {
//  def run[F[_]: C](f: K => F[V]): F[V]
//}

case class Task[C[_[_]], F[_]: C, K, V](r: (K => F[V]) => F[V])

object TasksObj {
  type Tasks[C[_[_]], F[_], K, V] = K => Option[Task[C, F, K, V]]
}


