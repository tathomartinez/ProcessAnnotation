package mela.pela

import Sale
import kotlin.String
import kotlin.Unit

public class SaleImpl : Sale {
  public fun total(): Unit {
    this.vender()
  }

  public override fun vender(): Unit {
    TODO()
  }

  public override fun toString(): String = StringBuilder().append("suck").toString()
}
