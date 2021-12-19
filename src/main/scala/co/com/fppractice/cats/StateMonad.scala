package co.com.fppractice.cats

import java.time.{DateTimeException, LocalDateTime}

import cats.Eval
import cats.data.{IndexedStateT, State}




object StateMonad extends App {

  val nextInt: State[Int, Int] =
    State(s => (s+4 , s * 2))
  def seq: State[Int, Int] =
    for {
      A1 <- nextInt //5 - 2
      A2 <- nextInt //9 - 10
      A3 <- nextInt //13 - 18
      A4 <- nextInt //17 - 26
    } yield A1 + A2 + A3 + A4

  val state2: State[Int, Int] = seq
  val state3: State[Int, Int] = seq

  println("STATE 3 STATUS")

  println(state3.runA(1).value) //  64 CORRE LA IZQUIERDA
  println(state3.runS(1).value) // 13   CORRE LA DERECHA

  println("STATE 2 STATUS")

  println(state2.runA(1).value) //  64 CORRE LA IZQUIERDA
  println(state2.runS(1).value) // 17   CORRE LA DERECHA


}

trait StateOrder
case object AddedToTheCar extends StateOrder
case object Ordered extends StateOrder
case object Payed extends StateOrder
case object InDelivery extends StateOrder
case object Deliveried extends StateOrder

case class Order(stateOrder: StateOrder,item:String)

object OrderService {

  private val nextState: State[Order, Either[String, Order]] = State(s => (OrderService.changeState(s), OrderService.save(s)))


  private def changeState(order: Order): Order ={
    println("Changing state")
    println(LocalDateTime.now())
    Thread.sleep(2000)
    println(Thread.currentThread().getName)
    println("Changing state")
    order.stateOrder match {
      case AddedToTheCar => order.copy(stateOrder = Ordered)
      case Ordered => order.copy(stateOrder = Payed)
      case Payed => order.copy(stateOrder = InDelivery)
      case InDelivery => order.copy(stateOrder = Deliveried)
    }

  }

  private def save(order:Order): Either[String,Order]= {
    println(s"Saving order----> $order in database")
    println(LocalDateTime.now())
    println(Thread.currentThread().getName)
    println(s"Saving order----> $order in database")
    Right(order)
  }

  def makeOrder(order:Order):Either[String,Order] = {
    println(s"Making an order with state: ${order.stateOrder} and item: ${order.item}")
    val (orderNexState,dbAnswer) = nextState.run(order).value
    dbAnswer
  }
}

object StateApplication extends App{

  def makeAnOrderEndpoint(order: Order): Either[String,Order] ={
    OrderService.makeOrder(order)
  }

  val order = Order(AddedToTheCar,"Chocorramo")

  val finalMessage = makeAnOrderEndpoint(order) match {
    case Right(order) => s"Order state changed $order"
    case Left(errorMessage) => errorMessage
  }


  println(finalMessage)
  //Esta aplicación modelará el estado de una orden con el siguiente flujo.
  //EnElCarrito -> Ordenada -> Pagada -> Delivery -> Entregada.



}
