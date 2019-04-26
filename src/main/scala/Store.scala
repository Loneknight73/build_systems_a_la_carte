package bsalc

case class Store[I, K, V] (info: I, values: K => V) // Info, Key, Value

object Store {

  def getInfo[I, K, V](s: Store[I, K, V]): I = s.info
  def getValue[I, K, V] (k: K)(s: Store[I, K, V]): V = s.values(k)
  def initialise[I, K, V] (i: I, v: K => V): Store[I, K, V] = new Store(i, v)
  def putInfo[I, K, V] (i: I, s: Store[I, K, V]): Store[I, K, V] = initialise(i, s.values)
  def putValue[I, K, V](k: K, v: V)(s: Store[I, K, V]): Store[I, K, V] = {
    def nv: (K => V) = {
      key => key match {
        case `k` => v
        case _  => s.values(key)
      }
    }
    initialise(s.info, nv)
  }
}

trait Hash[V] {
  def hash(v: V): Hash[V]
  def getHash[I, K] (k: K, s: Store[I, K, V]): Hash[V]
}

object TestStore extends App {
  import bsalc.Store._

  val s = initialise("a store", {k: String => k match {
    case "A1" => 10
    case "A2" => 20
    case _ => -1
  }})

  println(getInfo(s))
  println(getValue("A1")(s))
  println(getValue("A2")(s))
  println(getValue("A3")(s))
  val s2 = putValue("A3", 30)(s)
  println(getValue("A3")(s))
  println(getValue("A3")(s2))

}
