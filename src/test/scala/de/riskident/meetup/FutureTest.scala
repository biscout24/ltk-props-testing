package de.riskident.meetup

import cats.Monad
import cats.laws.discipline.MonadTests
import org.scalatest.FunSuite
import org.scalatest.Matchers
import org.typelevel.discipline.scalatest.Discipline
import cats.instances.int._
import cats.instances.tuple._
import cats.kernel.Eq
import cats.laws.discipline._
import org.scalacheck.Arbitrary.arbitrary

import org.scalacheck.Arbitrary
import org.scalacheck.Gen

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class FutureTest extends FunSuite with Matchers with Discipline {

  implicit val myOptionMonad: Monad[Future]  = new Monad[Future] {
    def pure[A](value: A): Future[A] = Future(value)

    def flatMap[A, B](opt: Future[A])(fn: A => Future[B]): Future[B] = opt.flatMap(fn)

    override def tailRecM[A, B](a: A)(f: A => Future[Either[A, B]]): Future[B] = {
      f(a).flatMap {
        case Right(r) => Future.successful(r)
        case Left(l) => tailRecM(l)(f)
      }
    }
  }

/*
  implicit def arbFuture[T](implicit a: Arbitrary[T]): Arbitrary[Future[T]] = {
    Arbitrary(arbitrary[T].map(v => Future(v)))
  }
*/

  implicit def eqFuture[T](implicit t: Eq[T]): Eq[Future[T]] = new Eq[Future[T]] {
    def eqv(x: Future[T], y: Future[T]): Boolean = {
      val result = for {
        xRes <- x
        yRes <- y
      } yield xRes == yRes
      Await.result(result, 5.seconds)
    }
  }

  checkAll("Future[Int]", MonadTests[Future].monad[Int, Int, Int])

}
