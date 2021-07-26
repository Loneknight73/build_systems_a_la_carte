package bsalc

abstract class Task[C[_[_]], K, V] {
  def run[F[_] : C](fetch: K => F[V]): F[V]
}

type Tasks[C[_[_]], K, V] = K => Option[Task[C, K, V]]

