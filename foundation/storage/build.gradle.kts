plugins {
    alias(libs.plugins.kotlin)
    `java-test-fixtures`
}

// Move to build plugin
group = "io.github.alaksion.invoicer.foundation.storage"
version = "0.0.1"

dependencies {
    implementation(libs.minIO)
    implementation(projects.foundation.secrets)

    testImplementation(libs.kotlin.test)
    implementation(libs.kodein.server)
}