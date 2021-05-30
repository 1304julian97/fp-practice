package co.com.fppractice

import co.com.fppractice.refined.RefinedTypes._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto.autoRefineV
import eu.timepit.refined.refineV
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class RefinedTypesSuite extends AnyFlatSpec with Matchers {

  it should "refined types test" in {
    var ageInput: Int = 12
    ageInput = 15
    val idNumberValidInput: Int = 100000001
    val idNumberInvalidInput: Int = 9999

    val age: Age = Age(1)
    //val age: Age = Age(-1)       // won't compile, Left predicate of (!(-1 < 1) && !(-1 > 150)) failed: Predicate (-1 < 1) did not fail.
    //val age: Age = Age(ageInput) // won't compile, compile-time refinement only works with literals

    val account: Either[String, UserAccount] = for {
      age <- refineV[AgeRestrictions](ageInput)
      idNumber <- refineV[IdNumberRestrictions](idNumberValidInput)
    } yield UserAccount(CC, IdNumber(idNumber), Age(age))

    account.isRight shouldBe true

    val accountWithError: Either[String, UserAccount] = for {
      age <- refineV[AgeRestrictions](ageInput)
      idNumber <- refineV[IdNumberRestrictions](idNumberInvalidInput)
    } yield UserAccount(CC, IdNumber(idNumber), Age(age))

    accountWithError.left.get shouldBe "Predicate failed: (9999 > 100000000)."
  }

}




















