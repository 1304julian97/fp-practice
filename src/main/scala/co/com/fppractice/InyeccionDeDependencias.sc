import java.util.UUID

import TestMocks.pagarServiceMock

trait OrderService {
  def ordenar(item: String): Orden
}
trait PagarService {
  def pagar(order: Orden): Int
  def notificarPago(order: Orden): Unit
}
case class Orden(id:String, item: String, v:Int)
// forma 1
class OrderServiceImp(pagarService: PagarService) extends OrderService {
  override def ordenar(item: String): Orden = {
    val order: Orden = Orden(UUID.randomUUID().toString, item, 23)
    pagarService.pagar(order)
    order
  }
}
class PagarServiceImp() extends PagarService {
  override def pagar(order: Orden): Int = {
    notificarPago(order)
    order.v
  }
  override def notificarPago(order: Orden): Unit = println("pagado")// llama servicio de prod
}
//forma 2
class AllServiceImp() extends OrderService with PagarService {
  override def ordenar(item: String) = {
    val order: Orden = Orden(UUID.randomUUID().toString, item, 23)
    pagar(order)
    order
  }
  override def pagar(order: Orden) = order.v
  override def notificarPago(order: Orden): Unit = () //enviar correo
}
//prod
val pagarServiceProd = new PagarServiceImp()
val orderServiceProd = new OrderServiceImp(pagarServiceProd)
orderServiceProd.ordenar("cafe")
object TestMocks {
  def pagarServiceMock(valor: Int = 0): PagarService = new PagarService {
    override def pagar(order: Orden): Int = order.v
    override def notificarPago(order: Orden): Unit =  println("pagado")
  }
}
object Test {
  //test orderservice 1
  val orderService = new OrderServiceImp(pagarServiceMock(0))
  orderService.ordenar("cafe")
  //test orderservice 2
  val orderService1 = new OrderServiceImp(pagarServiceMock(43))
  orderService.ordenar("cafe")
  //test orderservice 3
  val allService = new AllServiceImp( ){
    override def pagar(order: Orden) = super.pagar(order)
    //override def notificarPago(order: Orden): Unit = println("pagado")
  }
  orderService.ordenar("cafe")
  //test orderservice 4
  val allService1 = new AllServiceImp( ){
    override def pagar(order: Orden) = 43
    override def notificarPago(order: Orden): Unit = println("pagado")
  }
  orderService.ordenar("cafe")
}