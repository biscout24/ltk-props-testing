package de.riskident.meetup

import cats.Monad
import cats.instances.int._
import cats.instances.string._
import cats.instances.option._
import cats.instances.boolean._
import cats.instances.tuple._
import cats.kernel.Eq
import cats.laws.discipline._
import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.typelevel.discipline.scalatest.Discipline
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalacheck.Arbitrary
import org.scalactic.anyvals.PosInt

import scala.util.Failure
import scala.util.Success
import scala.util.Try

class TryTest extends FunSuite with Matchers with Discipline {

/*
  implicit override val generatorDrivenConfig: PropertyCheckConfiguration =
    PropertyCheckConfiguration(minSuccessful = 5000)
*/

  import de.riskident.meetup.Helpers.eqException
  import de.riskident.meetup.Helpers.arbAtoBwithThrow

  implicit val myTryMonad: Monad[Try] = new Monad[Try] {
    def pure[A](value: A): Try[A] = Try(value) //unit

    def flatMap[A, B](fa: Try[A])(f: A => Try[B]): Try[B] = fa.flatMap(f) //bind

    override def tailRecM[A, B](a: A)(f: A => Try[Either[A, B]]): Try[B] = {
      f(a).flatMap {
        case Right(r) => Success(r)
        case Left(l)  => tailRecM(l)(f)
      }
    }
  }

  //f: A => F[B]
  implicit def arbTry[T](implicit a: Arbitrary[T]): Arbitrary[Try[T]] = {
    Arbitrary(
      Gen.oneOf(
        arbitrary[T].map(v => Try(v)),
        Gen.const(
          Try(throw new IllegalStateException("arbitrary T"))
        )
      )
    )
  }

  implicit def eqTry[T](implicit t: Eq[T]): Eq[Try[T]] = new Eq[Try[T]] {
    def eqv(x: Try[T], y: Try[T]): Boolean = (x, y) match {
      case (Success(xv), Success(yv)) => xv == yv
      case (Failure(xe), Failure(ye)) => (xe.getClass == ye.getClass) && (xe.getMessage == ye.getMessage)
      case other => false
    }
  }


  checkAll("Try [String => Int] => Int => Option[Boolean]", MonadTests[Try].stackUnsafeMonad[Int => Int, Int, Int])
}
