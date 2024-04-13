import mela.pela.SaleImpl

@ImplementInterface
interface Sale {
    fun vender()
}

//@ImplementInterface
//interface Volar {
//    fun mover()
//}

fun main() {
    val venta = SaleImpl()
    venta.vender()
//    VolarImpl().mover()
}

class PaySaleImpl : SaleImpl() {
    fun pagar() {
        println("Pagando")
    }
}