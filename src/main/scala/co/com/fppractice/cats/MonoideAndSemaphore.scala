package co.com.fppractice.cats

import java.time.LocalDateTime

import cats.Monoid
import cats.effect.{Concurrent, ExitCode, IO, IOApp, Timer}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}


trait Monoide[A]{
  def ap(a1:A,a2:A):A

  def combineAll(lista:List[A]):A = lista.fold(empty)(ap)

  def empty:A

  def foldParallel(lista:A*):A
}


object CatsApp {
  val f = Monoid[String].combine("HOLA","JULIAN")
  val f2 = Monoid[String].combine("JULIAN","HOLA")

  def futureMonoideString:Monoide[Future[String]] = new Monoide[Future[String]] {
    override def ap(a1: Future[String], a2: Future[String]): Future[String] = {
      for{
        s1 <- a1
        s2 <- a2
      }yield s1+s2
    }

    override def empty: Future[String] = Future.successful("")

    override def foldParallel(lista: Future[String]*): Future[String] = ???
  }


  def stringMonoide:Monoide[String] = new Monoide[String] {
    override def ap(a1: String, a2: String): String = {
      if(a1.length < a2.length) a2+a1
      else a1+a2

    }

    override def empty: String = ""

    override def foldParallel(lista: String*): String = {

      def recursionSintax(lista2:List[Future[String]]): Future[String] ={
        if(lista2.length == 1) lista2.head
        else recursionSintax(lista2.grouped(2).map( x=> futureMonoideString.combineAll(x)).toList)
      }

      val finalResult = recursionSintax(lista.grouped(2).map(x=> Future(combineAll(x.toList))).toList)
      Await.result(finalResult,5.seconds)

    }
  }




}


object Main extends App{


  val f = CatsApp.stringMonoide.ap("HOLA","JULIAN")
  val f2 = CatsApp.stringMonoide.ap("JULIAN","HOLA")
  val f3 = CatsApp.stringMonoide.ap("JULIAN",CatsApp.stringMonoide.ap("CARVAJAL", "MONTOYA"))
  val f4 = CatsApp.stringMonoide.ap(CatsApp.stringMonoide.ap("JULIAN","CARVAJAL"),"MONTOYA")
  println(f)
  println(f2)
  println(f3)
  println(f4)


  val f5 = Monoid[String].combine("HOLA","JULIAN")
  val f6 = Monoid[String].combine("JULIAN","HOLA")
  val f7 = Monoid[String].combine("JULIAN",Monoid[String].combine("HOLA","CARVAJAL"))
  val f8 = Monoid[String].combine(Monoid[String].combine("JULIAN","HOLA"),"CARVAJAL")
  println(f5)
  println(f6)
  println(f7)
  println(f8)


  println("_________________STREAMS_________________")
  lazy val jkdf = {23+1}
  val jklds = 23+1


 /* val stream = 1 #:: 2 #:: 3 #:: Stream.empty

  println(stream.size)

  def factorial(a: Int, b: Int): Stream[Int] =  a #:: factorial(a*(b+1), b+1)
  val factorials7: Stream[Int] = factorial(1, 1).take(7)
  println(factorials7.size)
  val factorialsList = factorials7.toList
  println(factorialsList)
*/

  def intMethod:Int = { println("hola soy un método"); 1+41 }
  val streamv2 = (intMethod+1) #::  (intMethod+2) #:: (intMethod+3) #:: Stream.empty
  val streamv3 =Stream.empty.#::((intMethod+1)).#::((intMethod+2))
  println(streamv2.size)




  println("_________________call by name_________________")

  val intt  = { println("hi"); 1+41 }
  lazy val intt2  = { println("hi"); 1+41 }
  intt2
  def intt3  = { println("hi"); 1+41 }


  println("Antes")
  def maybeTwice(b: Boolean,  i: =>  Int) = if (b) i + i else 0
  println("Después")
  val x = maybeTwice(true, intt3 )
  println("Después de ejecución "+x)
}

/*
____________________SEMAPHORE
 */

import cats.effect.concurrent.Semaphore
import cats.syntax.all._

import scala.concurrent.ExecutionContext

// Needed for getting a Concurrent[IO] instance





object MainSemaphore extends IOApp{

  implicit val ctx = IO.contextShift(ExecutionContext.global)
  // Needed for `sleep`
  override implicit val timer = IO.timer(ExecutionContext.global)

  class PreciousResource[F[_]](name: String, s: Semaphore[F])(implicit F: Concurrent[F], timer: Timer[F]) {
    def use: F[Unit] = {
      println(Thread.currentThread().getName+">>>>>>>>>>>>>>"+name)
      for {
        x <- s.available
        _ <- F.delay(println(s"$name >> Availability: $x ->>>>>>>>>>>>>> with time: ${LocalDateTime.now.toString}" ))
        _ <- s.acquire
        y <- s.available
        _ <- F.delay(println(s"$name >> Started | Availability: $y ->>>>>>>>>>>>>> with time: ${LocalDateTime.now.toString}"))
        _ <- timer.sleep(5.seconds)
        //_ <- s.withPermit(someExpensiveTask)
        _ <- s.release
        z <- s.available
        _ <- F.delay(println(s"$name >> Done | Availability: $z  ->>>>>>>>>>>>>> with time: ${LocalDateTime.now.toString}"))
      } yield ()
    }
  }



  override def run(args: List[String]): IO[ExitCode] = {
    val program: IO[Unit] = {
      for {
        s  <- Semaphore[IO](2)
        r1 = new PreciousResource[IO]("R1", s)
        r2 = new PreciousResource[IO]("R2", s)
        r3 = new PreciousResource[IO]("R3", s)
        f2  <- List(r1.use, r2.use, r3.use).parSequence
      } yield ()
    }
    program.unsafeRunSync
    println("OEeeeeeeeeeee")
    Thread.sleep(50000)
    IO(ExitCode.Success)
  }
}