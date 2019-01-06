package org.bsalc

import org.bsalc.TasksObj.Tasks

object Build {

  type Build[C[_[_]], F[_], I, K, V] =
    (Tasks[C, F, K, V], K, Store[I, K, V]) => Store[I, K, V]
//  type Scheduler[C[_[_]], F, I, IR, K, V] =
//    Rebuilder[C[_], F, IR, K, V] => Build[C[_], F, I, K, V]
//  type Rebuilder[C[_[_]], F,    IR, K, V] =
//    (K, V, Task[C[_], F, K, V]) => Task[MonadState[IR], K, V]

}
