plugins {
    alias(libs.plugins.kotlin)
    `java-test-fixtures`
}

// Move to build plugin
group = "io.github.alaksion.invoicer.foundation.password.impl"
version = "0.0.1"

dependencies {
    implementation(libs.bcrypt)
    testImplementation(libs.kotlin.test)
    implementation(libs.kodein.server)
}