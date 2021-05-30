package co.com.fppractice.opaque

object ValueClasses {
  trait Measure extends Any
  case class AgeInYears(years: Int) extends AnyVal with Measure
  case class AgeInMonths(months: Int) extends AnyVal with Measure

  val ages: List[AgeInYears] = List(AgeInYears(1), AgeInYears(2)) // 1. Allocation will happen

  val isAdult: Boolean = ages.head match {                        // 2. Allocation will happen
    case AgeInYears(value) if value >= 18 => true
    case _ => false
  }

  //def add(a: Measure, b: Measure): Measure = ???                   // 3. if you call this method Allocation will happen

  def printlnAge(ageInMonths: AgeInMonths): Unit = println(s"ValueClasses - months: $ageInMonths")
  printlnAge(AgeInMonths(2))

  //def printlnAge(ageInYears: AgeInYears): Unit = println(s"years: $ageInYears") // won't compile, have the same type after erasure
  //printlnAge(AgeInMonths(2)) //double definition:

  //val age1 = 2 + AgeInYears(3)  // error: overloaded method value + with alternatives:
  //val age2 = AgeInYears(3) + 2  // error: type mismatch
   val age3: String = AgeInYears(3) + "aaa"  // scala/bug#8229
}
