package model

import NikaJsonClass

@NikaJsonClass
data class Cmd0110(
    val contextIndicator: String,
    val captureType: String,
    val track1: String?, // TODO[?] NOT used
    val track2: String?, // TODO[?] NOT used
    val encryptedPanSha1: String?, // TODO[?] NOT used
    val bin: String?,
    val last4Digits: String,
    val cardholderName: String?,
    val cardBrandName: String,
    val cardAbbreviation: String,
    val last4DigitsRequired: Boolean
)
//    : NikaModel("110")

//open class NikaModel(private val cmd: String)