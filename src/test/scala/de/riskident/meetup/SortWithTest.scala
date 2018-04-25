package de.riskident.meetup

import org.scalacheck.Gen
import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class SortWithTest extends FlatSpec with GeneratorDrivenPropertyChecks with Matchers {

  private def mySortFun(l: Int, r: Int): Boolean = { l <= r}

  "List.sortWith" should "produce the same list while sort twice" in forAll(minSuccessful(500)) { l: List[Int] =>
    l.sortWith(mySortFun) should be(l.sortWith(mySortFun).sortWith(mySortFun))
  }
}

import org.scalacheck.Arbitrary._
object GenTuple {

  def user: Gen[(String, Int)] = for {
    name     <- arbitrary[String]
    postcode <- arbitrary[Int]
  } yield (name, postcode)
}

