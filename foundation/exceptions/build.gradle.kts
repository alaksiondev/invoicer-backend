plugins {
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.serialization)
}

// Move to build plugin
group = "io.github.alaksion.invoicer.foundation.exceptions"
version = "0.0.1"

dependencies {
    implementation(libs.kotlin.datetime)
    implementation(libs.ktor.server.core.jvm)
    implementation(libs.kotlin.serialization)

    // Test
    testImplementation(libs.kotlin.test)
}