plugins {
    alias(libs.plugins.kotlin)
}

group = "io.github.alaksion.invoicer.server.domains.intermediary.domain.test"
version = "0.0.1"

dependencies {
    implementation(projects.domains.intermediary.domain.api)
}