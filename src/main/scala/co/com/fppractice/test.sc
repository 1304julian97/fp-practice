// 1. Typeclass
trait Suma[A] {
  def sumar(a: A, b: A): A
}
trait Resta[A] {
  def res//todo complete
}

// 2. Instance
val sumaString = new Suma[String] {
  override def sumar(a: String, b: String): String = //todo complete
}
val sumaInt = //todo complete

val restaString = new Resta[String] {
  override def restar(a: String, b: String): String = a.replace(b, "")
}
val restaInt = new Resta[Int] {}//todo complete


val resultado1: Int = sumaInt.sumar(1, 2)          // = 3
val resultado2: String = sumaString.sumar("1","2") // = 12


// 3. Syntax
implicit class IntSyntax(a: Int) {
  def +++(b: Int) //todo complete
  def ---(b: Int)(implicit resta: Resta[Int]): Int = resta //todo complete
}

implicit class StringSyntax(a: String) { } //todo complete

implicit val sumaIntInstance: Suma[Int]         = //todo complete
implicit val sumaStringInstance: Suma[String]   = //todo complete
implicit val restaIntInstance: Resta[Int]       = //todo complete
implicit val restaStringInstance: Resta[String] = //todo complete


val resultado3: Int    = 1 +++ 2
val resultado4: Int    = resultado3 --- 1
val resultado5: String = "1" +++ "2"
val resultado6: String = resultado5 --- "1"