package de.riskident.meetup

import cats.Monad
import cats.instances.int._
import cats.instances.tuple._
import cats.kernel.Eq
import cats.laws.discipline._
import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.typelevel.discipline.scalatest.Discipline

import scala.util.Failure
import scala.util.Success
import scala.util.Try

class TryNaiveTest extends FunSuite with Matchers with Discipline {

  import de.riskident.meetup.Helpers.eqException

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

  implicit def eqTry[T](implicit t: Eq[T]): Eq[Try[T]] = new Eq[Try[T]] {
    def eqv(x: Try[T], y: Try[T]): Boolean = (x, y) match {
      case (Success(xv), Success(yv)) => xv == yv
      case (Failure(xe), Failure(ye)) => (xe.getClass == ye.getClass) && (xe.getMessage == ye.getMessage)
      case other => false
    }
  }


  checkAll("Try Int => Int => Int", MonadTests[Try].stackUnsafeMonad[Int, Int, Int])
}
