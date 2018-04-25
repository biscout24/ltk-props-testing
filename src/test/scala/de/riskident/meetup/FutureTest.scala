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

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class FutureTest extends FunSuite with Matchers with Discipline {

  import de.riskident.meetup.Helpers.eqException
  //import de.riskident.meetup.Helpers.arbAtoBwithThrow

  implicit val myFutureMonad: Monad[Future]  = new Monad[Future] {
    def pure[A](value: A): Future[A] = Future(value)

    def flatMap[A, B](fa: Future[A])(f: A => Future[B]): Future[B] = fa.flatMap(f)

    override def tailRecM[A, B](a: A)(f: A => Future[Either[A, B]]): Future[B] = {
      f(a).flatMap {
        case Right(r) => Future.successful(r)
        case Left(l) => tailRecM(l)(f)
      }
    }
  }

  implicit def arbFuture[T](implicit a: Arbitrary[T]): Arbitrary[Future[T]] = {
    Arbitrary(
      Gen.oneOf(
        arbitrary[T].map(v => Future(v)),
        Gen.const(Future(throw new IllegalStateException("arbitrary value")))
      )
    )
  }

  implicit def eqFuture[T](implicit t: Eq[T]): Eq[Future[T]] = new Eq[Future[T]] {
    def eqv(x: Future[T], y: Future[T]): Boolean = {

      def handleError[A](fut: Future[A]) = fut.map(a => Right(a)).recover {
        case ex: Throwable => Left(ex)
      }

      val xValue: Future[Either[Throwable, T]] = handleError(x)
      val yValue: Future[Either[Throwable, T]] = handleError(y)


      val result: Future[Boolean] = for {
        xRes: Either[Throwable, T] <- xValue
        yRes: Either[Throwable, T] <- yValue
      } yield {
        (xRes, yRes) match {
          case (Right(xr), Right(yr)) => xr == yr
          case (Left(xr), Left(yr)) => (xr.getClass == yr.getClass) && (xr.getMessage == yr.getMessage)
          case _ => false
        }
      }
      Await.result(result, 5.seconds)
    }
  }

  checkAll("Future Int, Int, Option[Boolean]", MonadTests[Future].monad[Int, Int, Option[Boolean]])
}
