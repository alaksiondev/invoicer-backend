package io.github.alaksion.invoicer.server.app

import connectDatabase
import controller.rootController
import io.github.alaksion.invoicer.server.app.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    installDi()
    installAuth()
    connectDatabase()
    configureSerialization()
    installStatusPages()
    configureMonitoring()
    rootController()
}
