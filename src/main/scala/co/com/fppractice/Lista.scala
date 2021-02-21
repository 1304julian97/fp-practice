package co.com.fppractice

import co.com.fppractice.TypeClassInstance.listaIntInstance

sealed trait Lista[+A]
final case class ListaNoVacia[A](valor:A,lista:Lista[A]) extends Lista[A]
final case object ListaVacia extends Lista[Nothing]

trait ListaTypeClass[A]{
  def cabeza(lista:Lista[A]):A
  def cola(lista:Lista[A]):Lista[A]
}

trait EmptyTypeClass[A] {
  def isEmpty(lista: Lista[A])
}

object TypeClassInstance {
  def listaIntInstance[A]: ListaTypeClass[A] = new ListaTypeClass[A] {
    override def cabeza(lista: Lista[A]): A = lista match {
      case ListaNoVacia(value,_) => value
      case ListaVacia => throw new NoSuchElementException("Lista vacía")
    }
    override def cola(lista: Lista[A]): Lista[A] = lista match {
      case ListaNoVacia(_, lista) => lista
      case ListaNoVacia(_,ListaVacia) => ListaVacia
      case ListaVacia => throw new NoSuchElementException("Lista vacía")
    }
  }
}

object TypeClassInterface {
  //class Syntax
  implicit class ListaSyntax[A](lista:Lista[A]){
    def cabezaSyntax():A = listaIntInstance.cabeza(lista)
    def colaSyntax():Lista[A] = listaIntInstance.cola(lista)
  }
  //object Syntax
  object ListaSyntaxObj {

  }
}


object MyApp extends App {
  import TypeClassInterface._
  import TypeClassInstance._

  val lista: Lista[Int] = ListaNoVacia(1,ListaNoVacia(2,ListaVacia))
List()
  val cabeza = lista.cabezaSyntax()
  val cola = lista.colaSyntax()

 /* val cabeza = listaIntInstance.cabeza(lista)
  val cola = listaIntInstance.cola(lista)*/

  println(cabeza)
  println(cola)


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