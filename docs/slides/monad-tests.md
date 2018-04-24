### Is scala `Future[T]` a monad?
- [ ] Yes
- [ ] No
- [ ] What are you talking about?



### And Why?
```scala
import cats.Monad
import scala.concurrent.Future

new Monad[Future] {
    def pure[A](value: A): Future[A] = Future(value)

    def flatMap[A, B](fa: Future[A])(f: A => Future[B]): Future[B] = fa.flatMap(f)
}
```


### Laws
from Lecture 1.4 - Monads, [Functional Program Design in Scala](https://coursera.org/learn/progfun2)

* monad left identity
* monad right identity
* flatMap associativity


### Laws
```scala
  implicit override def F: Monad[F]

  def monadLeftIdentity[A, B](a: A, f: A => F[B]): IsEq[F[B]] =
    F.pure(a).flatMap(f) <-> f(a)

  def monadRightIdentity[A](fa: F[A]): IsEq[F[A]] =
    fa.flatMap(F.pure) <-> fa

  def flatMapAssociativity[A, B, C](fa: F[A], f: A => F[B], g: B => F[C]): IsEq[F[C]] =
    fa.flatMap(f).flatMap(g) <-> fa.flatMap(a => f(a).flatMap(g))
```


### “Different paths, same destination”
![](./img/property_commutative.png "commutative property")



### Show me some code