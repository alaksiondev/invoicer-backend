plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.serialization)
    `java-test-fixtures`
}

group = "io.github.alaksion.invoicer.consumers"
version = "0.0.1"

dependencies {
    implementation(projects.utils)
    implementation(projects.foundation.messaging)
    implementation(libs.kotlin.serialization)
    implementation(projects.services.api)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kodein.server)
    implementation(projects.foundation.log)
}
