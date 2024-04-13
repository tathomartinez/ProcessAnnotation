import mela.pela.SaleImpl

@ImplementInterface
interface Sale {
    fun vender()
}

fun main() {
    val venta = SaleImpl()
    venta.vender()
}