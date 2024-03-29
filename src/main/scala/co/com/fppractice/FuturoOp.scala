package co.com.fppractice



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

    def onTimeOut(function1: Function0[A]): A = {
      val result = future.andThen {
        case Success(value) => value
        case Failure(exception) => exception match {
          case t:TimeoutException => function1.apply()
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
          case _:TimeoutException => function1.apply()
          case _ => Future.failed(exception)
        }
      }
    }




  }
}

