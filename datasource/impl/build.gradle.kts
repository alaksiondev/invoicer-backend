plugins {
    alias(libs.plugins.kotlin)
}

// Move to build plugin
group = "datasource.impl"

dependencies {
    implementation(projects.datasource.api)
    implementation(projects.models)
    implementation(libs.kotlin.datetime)
    implementation(libs.bundles.exposed)
    implementation(libs.kodein.server)
    implementation(libs.postgres)
}
