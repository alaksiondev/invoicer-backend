package io.github.alaksion.invoicer.server.app

import controller.rootController
import io.github.alaksion.invoicer.consumers.InvoicerMessageConsumer
import io.github.alaksion.invoicer.server.app.database.connectDatabase
import io.github.alaksion.invoicer.server.app.plugins.*
import io.ktor.server.application.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    installDi()
    installAuth()
    connectDatabase()
    configureSecurity()
    configureSerialization()

    val clock by closestDI().instance<Clock>()
    installStatusPages(
        clock = clock
    )
    configureMonitoring()
    installWebSocket()
    rootController()

    val consumer by closestDI().instance<InvoicerMessageConsumer>()

    launch {
        consumer.consume()
    }
}
