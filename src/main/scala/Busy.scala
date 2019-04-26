//package bsalc

//import org.bsalc.Build.Build
//import org.bsalc.Task.Tasks
//import scalaz.{Applicative, State}
//
//object Busy {
//
//   def busy[K, V]: Build[Applicative, Unit, K, V] = {
//     def busyHelp[K, V](tasks: Tasks[Applicative, K, V],
//                        key: K,
//                        store: Store[Unit, K, V]): Store[Unit, K, V] = {
//        def fetch(k: K): State[Store[Unit, K, V], V] = {
//          val t = tasks(k)
//          t match {
//            case None => State.gets(Store.getValue(k))
//            case Some(task) => {
//              for {
//                v <- (task.getRun[State]())(fetch _)
//                _ <- State.modify(Store.putValue(k, v))
//              } yield v
//            }
//          }
//        }
//       val f= fetch(key)
//       val r = f(store)._1
//       r
//     }
//
//     busyHelp
//
//     /*
//      (tasks: Tasks[Applicative, State, K, V], key: K, store: Store[Unit, K, V]) => {
//      def fetch[K, V](k: K): State[Store[Unit, K, V], V] = {
//      val kk: K = k
//      val t: Tasks[Applicative, State, K, V] = tasks
//      val res: Option[Task[Applicative, State, K, V]] = t(kk)
//      res match {
//        case _ => State.gets(getValue(k))
//        case Some(task: Task[Applicative, State, K, V]) => for {
//          v <- task.run(fetch _)
//          _ <- State.modify(putValue(k, v))
//        } yield v
//
//              }
//            }
//            (fetch(key)).exec(store)
//          }
//        }
//     */
//  }
//}
