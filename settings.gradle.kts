enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
    }
}

rootProject.name = "invoicer-api"
include(":server")
include(":detekt")
include(":foundation:password:impl")
include(":foundation:password:test")
include(":foundation:authentication:impl")
include(":foundation:authentication:test")
include(":foundation:exceptions")
include(":foundation:secrets:impl")
include(":foundation:secrets:test")
include(":services:api")
include(":services:impl")
include(":services:test")
include(":repository:impl")
include(":repository:test")
include(":models")
include(":controller")
include(":foundation:env:impl")
include(":foundation:env:test")
include(":foundation:cache:impl")
include(":datasource:api")
include(":datasource:impl")
include(":foundation:qrcode")
include(":utils")