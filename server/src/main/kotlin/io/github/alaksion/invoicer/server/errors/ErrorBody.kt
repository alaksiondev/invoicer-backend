package io.github.alaksion.invoicer.server.errors

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class ErrorBody(
    val message: String,
) {
    val timeStamp = LocalDateTime.now().toString()
}