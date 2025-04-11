plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ktor)
    alias(libs.plugins.serialization)
    alias(libs.plugins.detekt)
}

group = "io.github.alaksion.invoicer.controller"
version = "0.0.1"

dependencies {
    implementation(libs.kotlin.datetime)
    implementation(libs.bundles.ktor)
    implementation(projects.services.api)
    implementation(libs.kodein.server)
    implementation(projects.foundation.authentication)
    implementation(projects.models)
    implementation(projects.foundation.exceptions)
    implementation(projects.utils)
}
