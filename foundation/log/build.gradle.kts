plugins {
    alias(libs.plugins.kotlin)
    `java-test-fixtures`
}

// Move to build plugin
group = "io.github.alaksion.invoicer.foundation.log"
version = "0.0.1"

dependencies {
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kodein.server)
    implementation(libs.bundles.logback)

    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.kotlin.test)
}