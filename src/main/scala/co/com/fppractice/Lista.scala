package co.com.fppractice


sealed trait Opcional[+A]
case class Algo[A](valor:A) extends Opcional[A]
case object Nada extends Opcional[Nothing]


object TypeClassOpcionalInstance{

  def emptyTypeClassInstanceOpcional: VacioTypeClass[Opcional] = new VacioTypeClass[Opcional] {
    override def estaVacio[A](obj: Opcional[A]): Boolean = obj match{
      case Nada => true
      case _ => false
    }
  }

}

sealed trait Lista[+A]
final case class ListaNoVacia[A](valor:A,lista:Lista[A]) extends Lista[A]
final case object ListaVacia extends Lista[Nothing]

trait CabezaColaTypeClass[A]{
  def cabeza(lista:Lista[A]):A
  def cola(lista:Lista[A]):Lista[A]
}

trait VacioTypeClass[F[_]] {
  def estaVacio[A](obj: F[A]):Boolean
  def noEstaVacio[A](obj: F[A]):Boolean = !estaVacio(obj)
}



object TypeClassInstance {
  def listaIntInstance[A]: CabezaColaTypeClass[A] = new CabezaColaTypeClass[A] {
    override def cabeza(lista: Lista[A]): A = lista match {
      case ListaNoVacia(value,_) => value
      case ListaVacia => throw new NoSuchElementException("Lista vacía")
    }
    override def cola(lista: Lista[A]): Lista[A] = lista match {
      case ListaNoVacia(_, lista) => lista
      case ListaVacia => throw new NoSuchElementException("Lista vacía")
    }
  }

  def emptyTypeClassInstanceLista: VacioTypeClass[Lista] = new VacioTypeClass[Lista] {

    override def estaVacio[A](obj: Lista[A]): Boolean = obj match{
      case ListaVacia => true
      case _ => false
    }
  }

  def emptyTypeClassInstanceOpcional: VacioTypeClass[Opcional] = new VacioTypeClass[Opcional] {
    override def estaVacio[A](obj: Opcional[A]): Boolean = obj match {
      case Algo(_) => false
      case Nada => true
    }
  }

}

object TypeClassInterface {

  import co.com.fppractice.TypeClassInstance.{emptyTypeClassInstanceLista, listaIntInstance,emptyTypeClassInstanceOpcional}
  //class Syntax
  implicit class ListaSyntax[A](lista:Lista[A]){
    def cabezaSyntax = listaIntInstance.cabeza(lista)
    def colaSyntax:Lista[A] = listaIntInstance.cola(lista)
    def estaVacio:Boolean = emptyTypeClassInstanceLista.estaVacio(lista)
    def noEstaVacio:Boolean = emptyTypeClassInstanceLista.noEstaVacio(lista)
  }

  implicit class OpcionalSintax[A](opcional: Opcional[A]){
    def estaVacio:Boolean = emptyTypeClassInstanceOpcional.estaVacio(opcional)
    def noEstaVacio:Boolean = emptyTypeClassInstanceOpcional.noEstaVacio(opcional)
  }
}


object MyApp extends App {
  import TypeClassInterface._

  val lista: Lista[Int] = ListaNoVacia(1,ListaNoVacia(2,ListaVacia))
  val cabeza = lista.cabezaSyntax
  val cola = lista.colaSyntax
  val novacio = lista.noEstaVacio
  val vacio = cola.colaSyntax.estaVacio
 /* val cabeza = listaIntInstance.cabeza(lista)
  val cola = listaIntInstance.cola(lista)*/

  println(cabeza)
  println(cola)
  println(novacio)
  println(vacio)
  println(cola.colaSyntax)

  println("Prueba opcional")

  val opcional:Opcional[String] = Algo("hola")
  println(opcional.estaVacio)



  /*
    val cabeza = lista.cabezaSyntax
    println(cabeza)
    val cola = lista.colaSyntax
    println(cola)
    val cola2 = cola.colaSyntax
    println(cola2)
    cola2.cabezaSyntax
  */

}