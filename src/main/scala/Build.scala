//package bsalc
//
//import bsalc.Task.Tasks
//
//object Build {
//
//  type Build[C[_[_]], I, K, V] = (Tasks[C, K, V], K, Store[I, K, V]) => Store[I, K, V]
////  type Scheduler[C[_[_]], F, I, IR, K, V] =
////    Rebuilder[C[_], F, IR, K, V] => Build[C[_], F, I, K, V]
////  type Rebuilder[C[_[_]], F,    IR, K, V] =
////    (K, V, Task[C[_], F, K, V]) => Task[MonadState[IR], K, V]
//
//}
