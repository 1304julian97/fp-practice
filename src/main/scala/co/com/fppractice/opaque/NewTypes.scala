package co.com.fppractice.opaque

import io.estatico.newtype.macros.newtype

object NewTypes {
  object AgeInYears {
    @newtype case class Opaque(years: Int)
  }

  object AgeInMonths {
    @newtype case class Opaque(months: Int)
    object Opaque {
      def unapply(newtype: Opaque): Option[Int] = Option(newtype).map(_.months)
    }
  }

  val ages: List[AgeInYears.Opaque] = List(AgeInYears.Opaque(1), AgeInYears.Opaque(2))   // 1. will allocation happen?

  val isAdult: Boolean = ages.head match {                          // 2. will allocation happen?
    case AgeInYears.Opaque(value) if value >= 18 => true
    case _ => false
  }

  //def add(a: Measure, b: Measure): Measure = ???                   // 3. no possible newtypes do not support inheritance; illegal supertypes: Measure

  def printlnAge(ageInMonths: AgeInMonths.Opaque): Unit = println(s"NewTypes - months: $ageInMonths")
  printlnAge(AgeInMonths.Opaque(2))

  //def printlnAge(ageInYears: AgeInYears.Opaque): Unit = println(s"years: $ageInYears")  // won't compile, have the same type after erasure
  //printlnAge(AgeInYears(2))

  //val age1 = 2 + AgeInYears(3)  // error: overloaded method value + with alternatives:
  //val age2 = AgeInYears(3) + 2  // error: type mismatch
  val age3 = AgeInYears.Opaque(3) + "aaa"  // scala/bug#8229
}