plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.serialization)
    `java-test-fixtures`
}

// Move to build plugin
group = "io.github.alaksion.invoicer.repository.api"
version = "0.0.1"

dependencies {
    implementation(projects.models)
    implementation(projects.datasource.api)
    implementation(projects.foundation.cache)
    implementation(libs.kotlin.serialization)

    implementation(libs.kodein.server)
    implementation(libs.kotlin.datetime)

    // Fixtures
    testFixturesImplementation(testFixtures(projects.models))
}
