import mela.pela.FacturaImpl
import mela.pela.SaleImpl

@ImplementInterface
interface Sale {
    fun vender()
}

@FacturaColombianas
open class Factura {
    val uid: String = "123"
}

//@ImplementInterface
//interface Volar {
//    fun mover()
//}

fun main() {
    val venta = PaySaleImpl()
    venta.vender()
//    var factura = FacturaImpl()
//    println(factura.uid)
//    VolarImpl().mover()
}

class PaySaleImpl : SaleImpl() {
    override fun vender() {
        println("Pagando")
    }
}
