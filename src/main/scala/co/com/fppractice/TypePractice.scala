package co.com.fppractice

import co.com.fppractice.MiAlgebra.Ayudante

case class TypePractice()

trait Lista[+A]

case class ListaNoVacia[A](valor:A,lista:Lista[A]) extends Lista[A]

case object Vacio extends Lista[Nothing]

trait MiAlgebra[A]{
  def cabeza[A](lista:Lista[A]):A
  def colaList[A](lista:Lista[A]):Lista[A]
}

object MiAlgebra {
  implicit class Ayudante[A](lista:Lista[A]){
    def cabeza:A = {
      lista match {
        case ListaNoVacia(value,_) => value
        case Vacio => throw new NoSuchElementException("Lista vacía")
      }
    }
    def colaLista:Lista[A] = {
      lista match {
        case ListaNoVacia(_, lista) => lista
        case ListaNoVacia(_,Vacio) => Vacio
        case Vacio => throw new NoSuchElementException("Lista vacía")
      }
    }

  }

}

trait DTO

case object ARBOL extends DTO
case object MADERA extends DTO
case object PINO extends DTO

object app extends App{
  val lista: Lista[Int] = ListaNoVacia(1,ListaNoVacia(2,Vacio))
  //val ayudante = new Ayudante(lista)
  val cabeza = lista.cabeza
  println(cabeza)
  val cola = lista.colaLista
  println(cola)
  val cola2 = cola.colaLista
  println(cola2)
  cola2.cabeza

  //DTO => ARBOL, PINO, MADERA
}