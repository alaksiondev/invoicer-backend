import buildlogic.plugins.AppConfig
import io.ktor.plugin.features.*

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ktor)
    alias(libs.plugins.serialization)
    alias(libs.plugins.detekt)
}

// Move to build plugin
group = "io.github.alaksion.invoicer.server"
version = AppConfig.version

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(libs.bundles.exposed)
    implementation(libs.kodein.server)
    implementation(libs.kotlin.datetime)
    implementation(libs.openpdf)
    implementation(libs.postgres)
    implementation(libs.h2)
    implementation(libs.bundles.logback)
    implementation(libs.bundles.ktor)
    implementation("org.webjars:jquery:3.2.1")
    implementation(libs.swagger)

    // Micrometer
    implementation(libs.micrometer.prometheus)

    // Foundation
    implementation(projects.foundation.password)
    implementation(projects.foundation.authentication)
    implementation(projects.foundation.exceptions)
    implementation(projects.foundation.secrets)
    implementation(projects.foundation.env)
    implementation(projects.foundation.cache)
    implementation(projects.foundation.qrcode)
    implementation(projects.foundation.storage)
    implementation(projects.foundation.messaging)
    implementation(projects.foundation.log)
    implementation(projects.utils)
    implementation(projects.consumers)

    // Datasource
    implementation(projects.datasource.impl)

    // Repository
    implementation(projects.repository)

    // Services
    implementation(projects.services.impl)

    // Models
    implementation(projects.models)

    // Controller
    implementation(projects.controller)

    testImplementation(libs.ktor.server.tests.jvm)
    testImplementation(libs.kotlin.test)

    // Include all projects into test report
    rootProject.subprojects.forEach {
        kover(it)
    }
}

// Move to build plugin
detekt {
    val configPath = rootDir.absolutePath + "/config/detekt/detekt.yml"
    config.setFrom(configPath)
}

kover {
    reports {
        filters {
            excludes {
                packages("**.di")
            }
        }
    }
}

ktor {
    docker {
        jreVersion.set(JavaVersion.VERSION_17)
        localImageName.set("alaksion/invoicer-api")
        imageTag.set(AppConfig.version)

        externalRegistry.set(
            DockerImageRegistry.dockerHub(
                appName = provider { "invoicer-api" },
                username = providers.environmentVariable("DOCKER_USERNAME"),
                password = providers.environmentVariable("DOCKER_PASSWORD")
            )
        )
    }
}