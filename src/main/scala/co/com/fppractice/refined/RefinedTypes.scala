package co.com.fppractice.refined

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.{Greater, Interval}
import io.estatico.newtype.macros.newtype

object RefinedTypes {

  trait UserTypeId
  case object CC extends UserTypeId
  case object CE extends UserTypeId

  type IdNumberPred = Int Refined IdNumberRestrictions
  type AgePred = Int Refined AgeRestrictions

  type IdNumberRestrictions = Greater[W.`100000000`.T]
  type AgeRestrictions = Interval.Closed[W.`1`.T, W.`150`.T]

  @newtype case class Age(value: AgePred)
  @newtype case class IdNumber(value: IdNumberPred)

  final case class UserAccount(typeId: UserTypeId, idNumber: IdNumber, age: Age)
/*  object UserAccount {
    def fromDB(userTypeId: String, idNumber) = {

    }
  }*/
}
