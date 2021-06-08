import cats.{Apply, Foldable, Monad, Monoid}
import cats.implicits.toFoldableOps


Monoid[Map[String, Int]].combineAll(List(Map("a" -> 1, "b" -> 2), Map("a" -> 3)))
Monoid[Map[String, Int]].combineAll(List())
val l = List(1, 2, 3, 4, 5)
l.foldMap(i => (i, i.toString))

val intToString: Int => String = _.toString

Apply[Option].ap(Some(intToString))(Some(1))

Apply[Option].tuple2(Some(1), Some(2))

import cats.{Apply, Monad, Monoid}
import cats.implicits._
val option2 = (Option(1), Option(2))
val option3 = (option2._1, option2._2, Option.empty[Int])

val addArity2 = (x:Int,y:Int) => x+y
option2 mapN addArity2
option2 apWith Some(addArity2)

option2.tupled

Monad[Option].pure(1)


Monad[Option].ifM(Option(true))(Option("truthy"), Option("falsy"))
Monad[List].ifM(List(false))(List("truthy"), List("falsy"))

Monad[List].ifM(List(true, false, true))(List(1, 2), List(3, 4))


import cats._
val lazyResult =
  Foldable[List].foldRight(List(1, 2, 3), Now(0))((x, rest) => Later(x + rest.value))
lazyResult.value

Foldable[List].foldK(List(List(1, 2), List(3, 4, 5)))

Foldable[List].foldK(List(None, Option("two"), Option("three")))

Foldable[List].toList(List(1, 2, 3))
Foldable[Option].toList(Option(42))

Foldable[Option].filter_(Option(42))(_ != 42)

val FoldableListOption = Foldable[List].compose[Option]
FoldableListOption.fold(List(Option("1"), Option("2"), None, Option("3")))

Foldable[List].isEmpty(List(1, 2, 3))
Foldable[List].dropWhile_(List(1, 2, 3))(_ < 2)
Foldable[List].takeWhile_(List(1, 2, 3))(_ < 2)
