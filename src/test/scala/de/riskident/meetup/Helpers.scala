package de.riskident.meetup

import cats.kernel.Eq
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen

object Helpers {

  implicit def eqException[Exception]: Eq[Exception] = new Eq[Exception] {
    def eqv(x: Exception, y: Exception): Boolean = {
      x.getClass == y.getClass
    }
  }

  implicit def arbAtoBwithThrow[A, B](implicit a: Arbitrary[A], b: Arbitrary[B]): Arbitrary[A => B] = {
    Arbitrary(
      Gen.oneOf(
        for {
          a <- arbitrary[A]
          b <- arbitrary[B]
        } yield {a: A => b},
        Gen.const(
          {a: A => throw new IllegalStateException("arbitrary [A => B]")}
        )
      )
    )
  }
}
