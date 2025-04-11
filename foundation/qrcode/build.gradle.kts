plugins {
    alias(libs.plugins.kotlin)
}

// Move to build plugin
group = "foundation.qrcode"
version = "0.0.1"

dependencies {
    implementation(libs.zxing.javase)
    testImplementation(libs.kotlin.test)
    implementation(libs.kodein.server)
}