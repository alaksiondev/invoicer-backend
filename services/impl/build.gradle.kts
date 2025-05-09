plugins {
    alias(libs.plugins.kotlin)
    `java-test-fixtures`
    alias(libs.plugins.serialization)
}

// Move to build plugin
group = "io.github.alaksion.invoicer.service.impl"
version = "0.0.1"

dependencies {
    implementation(libs.kotlin.datetime)
    implementation(libs.kodein.server)

    implementation(projects.models)
    implementation(projects.repository)
    implementation(projects.services.api)
    implementation(projects.foundation.storage)
    implementation(projects.foundation.messaging)

    implementation(libs.pdf.itext)
    implementation(projects.foundation.exceptions)
    implementation(projects.foundation.authentication)
    implementation(projects.foundation.password)
    implementation(projects.foundation.qrcode)
    implementation(projects.foundation.log)
    implementation(projects.utils)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.serialization)
    implementation(projects.foundation.identityProvider)

    // Fixtures
    testImplementation(testFixtures(projects.models))
    testImplementation(testFixtures(projects.utils))
    testImplementation(testFixtures(projects.repository))
    testImplementation(testFixtures(projects.foundation.authentication))
    testImplementation(testFixtures(projects.foundation.password))
    testImplementation(testFixtures(projects.foundation.messaging))
    testImplementation(testFixtures(projects.services.api))
    testImplementation(testFixtures(projects.foundation.log))
    testImplementation(testFixtures(projects.foundation.qrcode))
    testImplementation(testFixtures(projects.foundation.identityProvider))

    // Unit Tests
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.coroutines.test)
}
