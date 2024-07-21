package io.github.alaksion.invoicer.server.utils

import java.text.NumberFormat
import java.util.*

fun Long.formatUsAmount(): String {
    val format = NumberFormat.getCurrencyInstance(Locale.US)
    return format.format(this)
}