package model

import NikaJsonClass

@NikaJsonClass
data class Cmd0100(
    val localCommerceOnUs: String,
    val binRequired: Boolean = false,
    private val isOnlineTransaction: Any? = null,
    private val selfService: Any? = null,
    val amount: Amount,
    val currencyCode: String = "CL",
    val cardType: String?,
    val listAmountsOfChange: Any? = null,
    val amountChange: Float? = null,
    val amountTip: Float? = null
)
typealias Amount = Long