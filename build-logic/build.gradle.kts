plugins {
    `kotlin-dsl`
    `version-catalog`
    `java-gradle-plugin`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.kover.gradle)
    implementation(libs.detekt.gradle)
}

gradlePlugin {
    plugins {
        create("invoicer-detekt") {
            id = "invoicer.detekt"
            implementationClass = "buildlogic.plugins.InvoicerDetektPlugin"
        }
    }
}