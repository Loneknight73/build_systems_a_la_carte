package bsalc

import org.scalacheck.*
import Store.*
import BuildSystems.busy
import Make.{make, MakeInfo}
import Ordering.String
import Spreadsheet.{sprsh1, sprsh2}

class BsalcSuite extends munit.FunSuite :

  test("Busy build system") {
    def kvFunc(k: String): Int = {
      val x = if (k == "A1") then 10 else 20
      x
    }

    val store: Store[Unit, String, Int] = initialise((), kvFunc)
    val result: Store[Unit, String, Int] = busy(sprsh1, "B2", store)
    val b1: Int = getValue("B1")(result)
    println(b1)
    println(getValue("B2")(result))
  }

  test("Make build system") {

    def kvFunc(k: String): Int = {
      val x = if (k == "A1") then 10 else 20
      x
    }

    val x = Ordering.String
    val store: Store[MakeInfo[String], String, Int] = initialise((0, Map.empty), kvFunc)
    // Not clear why it is necessary to explicitly pass Ordering.String here
    val result: Store[MakeInfo[String], String, Int]  = make(using Ordering.String)(sprsh1, "B2", store)
    val b1: Int = getValue("B1")(result)
    println(b1)
    println(getValue("B2")(result))
  }
