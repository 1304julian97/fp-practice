package co.com.fppractice

import co.com.fppractice.FutureOps.retry
import org.scalatest.concurrent.Futures
import org.scalatest.concurrent.PatienceConfiguration.{Interval, Timeout}
import org.scalatest.concurrent.ScalaFutures.convertScalaFuture
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import org.scalatest.time.{Millis, Minutes, Span}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.util.{Failure, Success}

object FutureOps {
  def retry[A](f: () => Future[A])(implicit delay: List[FiniteDuration]): Future[A] = {
    f() recoverWith {
      case error if delay.nonEmpty =>
        println(s"${error.getMessage}, Recovering delay:${delay.head.toMillis}, retries left:${delay.size}")
        Thread.sleep(delay.head.toMillis)
        retry(f)(delay.tail)
    }
  }
}

class DeployTestAnyFlatSuite extends AnyFlatSpec with Futures {
  import FutureOps._

  implicit val retries: List[FiniteDuration] = List(1 second, 3 second, 5 second)
  implicit val timeout: Timeout = Timeout(scaled(Span(7, Minutes)))
  implicit val interval: Interval = Interval(scaled(Span(150, Millis)))

  val externalResourceA = new ExternalServiceClient("Resource 1", 3)
  val externalResourceB = new ExternalServiceClient("Resource 2", 6)

  private def cleanUpServices: Future[List[Unit]] = {
    Future.sequence(
      List(
        retry(() => externalResourceB.setValue("default").map(_ => println("Resource 2: cleaned."))),
        retry(() => externalResourceA.setValue("default").map(_ => println("Resource 1: cleaned.")))
      )
    )
  }

  it should "test retries and clean up" in {
    val result = (for {
      _ <- externalResourceA.setValue("value 1")
      _ <- externalResourceB.setValue("value 2")
      value1 <- externalResourceA.getValue
      value2 <- externalResourceB.getValue
    } yield (value1, value2))
      .map(_ shouldBe ("condition 1", "condition 2"))
      .transformWith {
        case Success(assertion) =>
          for {
            _ <- cleanUpServices
            _ <- externalResourceA.getValue.map(_ shouldBe "default")
            _ <- externalResourceB.getValue.map(_ shouldBe "default")
          } yield assertion
        case Failure(error) =>
          for {
            _ <- cleanUpServices
            _ <- externalResourceA.getValue.map(_ shouldBe "default")
            _ <- externalResourceB.getValue.map(_ shouldBe "default")
          } yield throw error
      }
    result.futureValue(timeout, interval)
  }

}

final class ExternalServiceClient(name: String, attempts: Int)(implicit retries: List[FiniteDuration]) {

  private var state: Map[String, String] = Map("value" -> "default")
  private var currentAttempt = attempts

  def setValue(value: String): Future[Unit] = retry(() =>
    Future {
      if (currentAttempt == 0) {
        state = Map("value" -> value)
        println(s"$name: State modified to $value.")
      } else {
        currentAttempt -= 1
        throw new Exception(s"$name: error changing State to $value. (${currentAttempt + 1})")
      }
    }
  )
  def getValue: Future[String] = retry(() =>
    Future {
      val value = state.getOrElse("value", "error")
      println(s"$name: getting value $value.")
      value
    }
  )
}
