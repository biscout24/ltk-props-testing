Is scala `scala.util.Try[T]` a monad?
- [ ] Yes
- [ ] No
- [ ] What are you talking about?



### And Why?
```scala
import cats.Monad
import scala.util.Try

implicit val myTryMonad: Monad[Try] = new Monad[Try] {
    def pure[A](value: A): Try[A] = Try(value) //unit

    def flatMap[A, B](fa: Try[A])(f: A => Try[B]): Try[B] = fa.flatMap(f) //bind
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