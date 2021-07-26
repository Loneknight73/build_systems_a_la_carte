package bsalc

case class Store[I, K, V] (info: I, values: K => V) // Info, Key, Value

object Store {
  def getInfo[I, K, V](s: Store[I, K, V]): I = s.info
  def getValue[I, K, V] (k: K)(s: Store[I, K, V]): V = {
    val x = s.values(k)
    x
  }
  def initialise[I, K, V] (i: I, v: K => V): Store[I, K, V] = Store(i, v)
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

case class Hash[V] (v: V)

object Hash {
  def hash[V](v: V): Hash[V] = Hash(v)
  def getHash[I, K, V] (k: K, s: Store[I, K, V]): Hash[V] = Hash(Store.getValue(k)(s))
}

object TestStore extends App {
  import bsalc.Store._

  val s = initialise("a store", { k => k match {
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
