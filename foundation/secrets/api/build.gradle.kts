plugins {
    alias(libs.plugins.kotlin)
}

// Move to build plugin
group = "io.github.alaksion.invoicer.foundation.secrets.api"
version = "0.0.1"

dependencies {
    implementation(libs.kodein.server)
    implementation(libs.dotenv)
}