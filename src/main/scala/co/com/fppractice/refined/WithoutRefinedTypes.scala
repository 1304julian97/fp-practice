package co.com.fppractice.refined

import io.estatico.newtype.macros.newtype

object WithoutRefinedTypes {

  trait UserTypeId
  case object CC extends UserTypeId
  case object CE extends UserTypeId

  @newtype case class Age private(value: Int)
  object Age {
    def apply(value: Int): Either[String, Age] = Either.cond[String, Age](value > 1 && value < 150, new Age(value), "error")
  }

  @newtype case class IdNumber private(value: Int)
  object IdNumber {
    def apply(value: Int): Either[String, IdNumber] =
      Either.cond[String, IdNumber](value > 100000000, new IdNumber(value), s"Predicate failed: ($value > 100000000)")
  }

  final case class UserAccount(typeId: UserTypeId, idNumber: IdNumber, age: Age)

}
