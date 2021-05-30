package co.com.fppractice

import co.com.fppractice.NewTypesExample.{Foo, Foo2, metodo1, metodo2, metodo3}
import co.com.fppractice.PersonaCarateristicas.{Apellido, Edad, Nombre}
import io.estatico.newtype.macros.{newsubtype, newtype}



package object NewTypesExample {


  @newsubtype case class Foo(x: Int)
  @newtype case class Foo2(x: Int)


  def metodo1(foo:Foo) = {
    println(foo.x)
  }

  def metodo2(intt:Int) = {
    println(intt)
  }

  def metodo3(i:Integer) = {
    println(i)
  }
}

package object PersonaCarateristicas{

  @newsubtype case class Nombre(s:String)
  object Nombre {
    def apply(s:String): Nombre ={
      Nombre(s.capitalize)
    }
  }
  @newsubtype case class Apellido(s:String)
  @newsubtype case class Edad(i:Int)
  object Edad {
    def apply(i:Int): Edad ={
      if(i < 0) throw new Exception("aprenda a escribir edades pendejo")
      else Edad(i)
    }
  }

  def crearPersonaUnSafe(nombre:String,apellido:String, edad:Int):PersonaUnSafe = {
    PersonaUnSafe(apellido, nombre.capitalize, edad)
  }

  def crearPersona(nombre:Nombre,apellido:Apellido, edad:Edad):Persona = {
    Persona(nombre,apellido, edad)
  }


  def registrarseUnSafe(personaUnSafe: PersonaUnSafe) = {

  }

  //TESTS

  def prueba1CreatePersonaUnSafe()= {
    val esperado = PersonaUnSafe("Julian", "Carvajal", 23)
    esperado == crearPersonaUnSafe("Julian","Carvajal",23)
  }

  def prueba1CreatePersonaSafe()= {
    val esperado = Persona(Nombre("Julian"),Apellido( "Carvajal"), Edad(23))
    esperado == crearPersona(Nombre("Julian"),Apellido( "Carvajal"), Edad(23))
  }



}


case class Persona(nombre:Nombre,apellido:Apellido,edad:Edad)
case class PersonaUnSafe(nombre:String,apellido:String,edad:Int)


object Main7878 extends App{

  //val persona = Persona("Julian","Carvajal",23)
  val persona = Persona(Nombre("Julian"),Apellido("Carvajal"),Edad(23))

  println(persona.nombre.s)
  println(persona.nombre+persona.apellido+"adfaf")
  val x1 = Foo(2)
  val x2 = Foo(23)
  val x3 = Foo2(23)

  println(x1.getClass)
  metodo1(x1)
  metodo2(x1)
  metodo1(x2)
  metodo2(24)

}
