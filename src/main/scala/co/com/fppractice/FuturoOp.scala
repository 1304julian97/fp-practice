package co.com.fppractice

import java.util.concurrent.TimeoutException

import co.com.fppractice.FuturoOp.FutureSintax

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future, TimeoutException}
import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.util.{Failure, Success, Try}



object FuturoOp {

  implicit class FutureSintax[A](future:Future[A]){

    def waitUntil(time:FiniteDuration)(pf: PartialFunction[Throwable, A]): A ={
      val result = Try(Await.result(future,time))
      result match {
        case Success(value) => value
        case Failure(exception) => pf.apply(exception)
      }
    }

    def waitUntil(time:FiniteDuration):Future[A] = {
      val result = Try(Await.result(future,time))
      Future.fromTry(result)
    }

    def onTimeOut[B](function1: Function1[Unit,B]): B = {
      val result = future.andThen {
        case Success(value) => value
        case Failure(exception) => exception match {
          case TimeoutException => function1.apply()
          case _ => throw exception
        }
      }
      Try(Await.result(result, 2 seconds)) match {
        case Success(value) => value
        case Failure(exception) => throw exception
      }
    }

    def onTimeOutFuture(function1: Function1[Unit,Future[Any]]):Future[Any] ={
      future.andThen{
        case Success(value) => Future.successful(value)
        case Failure(exception) => exception match {
          case TimeoutException => function1.apply()
          case _ => Future.failed(exception)
        }
      }
    }




  }
}


object Main extends App{

  /*val future = Future{
    Thread.sleep(5000)
    "hola"
  }

  val f = future.waitUntil(2.seconds).onComplete{
    case Success(value) => println(value)
    case Failure(exception) => println(exception.getMessage)
  }

  future.recover()

  println(f)

*/

  /*package object opaquetypes {
    opaque type Cilindraje = Double

    object Cilindraje{
      val hola = "adasdf"
    }
  }

  case class Carro(marca:Marca, cilindraje:Cilindraje)

*/
}