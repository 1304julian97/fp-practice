package co.com.fppractice.opaque

object TaggedTypes {
  object AgeInYears {
    trait Tag
    type Base = Any {
      type thisIsNeededNotSureWhy
    }
    type Opaque = Base with Tag
    object Opaque {
      def apply(value: Int): Opaque = value.asInstanceOf[Opaque] //type casting only happens at compile time
      def unapply(opaque: Opaque): Option[Int] = Option(opaque).map(_.value)
    }
    final implicit class Ops(private val opaque: Opaque) extends AnyVal {
      def value: Int = opaque.asInstanceOf[Int]
    }
  }
  object AgeInMonths {
    trait Tag
    type Base = Any {
      type thisIsNeededNotSureWhy
    }
    type Opaque = Base with Tag
    object Opaque {
      def apply(value: Int): Opaque = value.asInstanceOf[Opaque] //type casting only happens at compile time
      def unapply(opaque: Opaque): Option[Int] = Option(opaque).map(_.value)
    }
    final implicit class Ops(private val opaque: Opaque) extends AnyVal {
      def value: Int = opaque.asInstanceOf[Int]
    }
  }

  val ages: List[AgeInYears.Opaque] = List(AgeInYears.Opaque(1), AgeInYears.Opaque(2)) // 1. will allocation happen?

  val isAdult: Boolean = ages.head match {                                             // 2. will allocation happen?
    case AgeInYears.Opaque(value) if value >= 18 => true
    case _ => false
  }

  //def add(a: Measure, b: Measure): Measure = ???                                        // 3. is this possible?

  def printlnAge(ageInMonths: AgeInMonths.Opaque): Unit = println(s"TaggedTypes - months: $ageInMonths")
  printlnAge(AgeInMonths.Opaque(2))

  //def printlnAge(ageInYears: AgeInYears.Opaque): Unit = println(s"years: $ageInYears") // won't compile, have the same type after erasure
  //printlnAge(AgeInYears.Opaque(2))

  //val age1 = 2 + AgeInYears.Opaque(3)  // error: overloaded method value + with alternatives:
  //val age2 = AgeInYears.Opaque(3) + 2 // error: type mismatch
  val age3: String = AgeInYears.Opaque(3) + " aaa" // scala/bug#8229

}