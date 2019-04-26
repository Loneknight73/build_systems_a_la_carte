//package bsalc
//import scalaz._

//case class TaskHelp[F[_], K, V](p: ((K => F[V]) => F[V]))
//
//class Task[C[_[_]], K, V]() {
//  type F[_]
//  type RunType = ((K => F[V]) => F[V])
//
//  var run: RunType = ???
//
//  def apply[F[_]: C](f: RunType) = {
//    run = f
//  }
//
//  def getRun[F[_]: C](): ((K => F[V]) => F[V]) = {
//    run
//  }
//}
//
//object Task {
//  type Tasks[C[_[_]], K, V] = (K => Option[Task[C, K, V]])
//}
//
//abstract class Task[C[_[_]], K, V] {
//  def run[F[_]: C](fetch: K => F[V]): F[V]
//}
//
//abstract class ApplicativeTask[K, V] extends Task[Applicative, K, V] {
//  def run[F[_]: Applicative](fetch: K => F[V]): F[V]
//}
//
//case class ApplicativeTaskHelper[F[_], K, V]() {
//  def apply(f: ((K => F[V]) => F[V])): ApplicativeTask[K, V] = new ApplicativeTask[K, V] {
//    def run[F[_]: Applicative](fetch: K => F[V]): F[V] = {
//      f(fetch)
//    }
//  }
//}
//
//case class ApplicativeTaskHelper2[F[_], K, V](f: ((K => F[V]) => F[V])) {
//  def apply(): ApplicativeTask[K, V] = new ApplicativeTask[K, V] {
//    def run[F[_]: Applicative](fetch: K => F[V]): F[V] = {
//      f(fetch)
//    }
//  }
//}
//
//object TestTask extends App {
//  val ath = ApplicativeTaskHelper[Applicative, String, Int] ()
//  val t1: ApplicativeTask[String, Int] = ath(g => g("B1"))
////  val t2 = ApplicativeTaskHelper2[Applicative, String, Int]
////  ( (f: (String => Applicative[Int]) => ({x: Int => x * 2} <*> f("B1"))))
//}