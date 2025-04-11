plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.serialization)
}

// Move to build plugin
group = "foundation.cache.impl"
version = "0.0.1"

dependencies {
    implementation(libs.kotlin.datetime)
    implementation(libs.kodein.server)
    implementation(libs.kotlin.serialization)
    implementation(libs.kotlin.coroutines.core)

    // Redis
    implementation(libs.jedis)

    // Project
    implementation(projects.foundation.exceptions)
    implementation(projects.foundation.secrets)
    implementation(projects.foundation.log)

    // Test
    testImplementation(libs.kotlin.test)
}