// 1. Typeclass
trait Suma[A] {
  def sumar(a: A, b: A): A
}
trait Resta[A] {
  def restar(a: A, b: A): A//todo complete
}

// 2. Instance
val sumaString = new Suma[String] {
  override def sumar(a: String, b: String): String = a+b//todo complete
}
val sumaInt = new Suma[Int]{
  override def sumar(a: Int, b: Int) = a+b
}//todo complete

val restaString = new Resta[String] {
  override def restar(a: String, b: String): String = a.replace(b, "")
}
val restaInt = new Resta[Int] {
  override def restar(a: Int, b: Int) = a-b
}//todo complete


val resultado1: Int = sumaInt.sumar(1, 2)          // = 3
val resultado2: String = sumaString.sumar("1","2") // = "12"
val resultado3:Int = restaInt.restar(1,2) // = -1
val resultado4:String = restaString.restar("12","2") // = "1"


// 3. Syntax
implicit class IntSyntax(a: Int) {
  def +++(b: Int) = sumaInt.sumar(a,b) //todo complete
  def ---(b: Int)(implicit resta: Resta[Int]): Int = resta.restar(a,b) //todo complete
}

implicit class StringSyntax(a: String) {
  def +++(b: String):String = sumaString.sumar(a,b)
  def ---(b:String)(implicit resta:Resta[String]) = resta.restar(a,b)
} //todo complete

implicit val sumaIntInstance: Suma[Int]         = sumaInt//todo complete
implicit val sumaStringInstance: Suma[String]   = sumaString//todo complete
implicit val restaIntInstance: Resta[Int]       = restaInt//todo complete
implicit val restaStringInstance: Resta[String] = restaString//todo complete

val restaInstanceTest:Resta[Int] = new Resta[Int] {
  override def restar(a: Int, b: Int) = 0
}

val resultado5: Int    = 1 +++ 2
val resultado6: Int    = resultado5.---(1)(restaInstanceTest)
val resultado7: String = "1" +++ "2"
val resultado8: String = resultado7 --- "1"
