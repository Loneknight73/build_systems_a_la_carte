package bsalc

import cats.{Applicative, Monad}
import cats.implicits._

object Spreadsheet {

  def sprsh1: Tasks[Applicative, String, Int] = { (k: String) =>
    k match {
      case "B1" => Some(new Task[Applicative, String, Int] {
        def run[F[_]: Applicative](fetch: String => F[Int]) = { (fetch("A1"), fetch("A2")).mapN(_ + _) }
      })
      case "B2" => Some(new Task[Applicative, String, Int] {
        def run[F[_]: Applicative](fetch: String => F[Int]) = { fetch("B1").map(_ * 2) }
      })
      case _ => None
    }
  }

  def sprsh2: Tasks[Monad, String, Int] = { (k: String) =>
    k match {
      case "B1" => Some(new Task {
        def run[F[_]: Monad] (fetch: String => F[Int]) = {
          for {
            c1 <- fetch("C1")
            x  <- if (c1 == 1) then fetch("B2") else fetch("A2")
          } yield x
        }
      })
      case "B2" => Some(new Task {
        def run[F[_]: Monad] (fetch: String => F[Int]) = {
          for {
            c1 <- fetch("C1")
            x  <- if (c1 == 1) then fetch("A1") else fetch("B1")
          } yield x
        }
      })
      case _ => None
    }
  }
}