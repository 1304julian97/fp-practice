package co.com.fppractice

import co.com.fppractice.refined.WithoutRefinedTypes._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class WithoutRefinedTypesSuite extends AnyFlatSpec with Matchers {

  it should "refined types test" in {
    val ageInput: Int = 12
    val idNumberValidInput: Int = 100000001
    val idNumberInvalidInput: Int = 9999

    val age: Either[String, Age] = Age(1)

    val account: Either[String, UserAccount] = for {
      age <- Age(ageInput)
      idNumber <- IdNumber(idNumberValidInput)
    } yield UserAccount(CC, idNumber, age)

    account.isRight shouldBe true

    val accountWithError: Either[String, UserAccount] = for {
      age <- Age(ageInput)
      idNumber <- IdNumber(idNumberInvalidInput)
    } yield UserAccount(CC, idNumber, age)

    accountWithError.left.get shouldBe "Predicate failed: (9999 > 100000000)."
  }

}

























