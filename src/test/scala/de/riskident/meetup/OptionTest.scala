package de.riskident.meetup

import cats.laws.discipline.MonadTests
import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.typelevel.discipline.scalatest.Discipline
import cats.instances.option._
import cats.kernel.Eq
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen


class OptionTest extends FunSuite with Matchers with Discipline {

  import de.riskident.meetup.Helpers.eqException
//  import de.riskident.meetup.Helpers.arbAtoBwithThrow

  checkAll("Option Int, Int, Int", MonadTests[Option].stackUnsafeMonad[Int => Int, Int, Int])
}
