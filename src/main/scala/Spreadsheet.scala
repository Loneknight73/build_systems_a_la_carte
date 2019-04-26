//package bsalc
//
//import org.bsalc.Task.Tasks
//import scalaz.Applicative
//
//
//class Spreadsheet[F[_]: Applicative] {
//
//  def b1f(fetch: String => F[Integer]): F[Integer] = {
//    fetch("A1")
//  }
//
//  def b2f(fetch: String => F[Integer]): F[Integer] = {
//    fetch("B1")
//  }
//
//  def sprsh1: Tasks[Applicative, String, Integer] = {
//
//    k => k match {
//        //case "B1" => Some(Task(b1f _))
//        //case "B2" => Some(Task(b2f _))
//        case _ => None
//      }
//  }
//}
//
