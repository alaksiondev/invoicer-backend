enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
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
include(":foundation:password:api")
include(":foundation:password:test")
include(":foundation:authentication:api")
include(":foundation:authentication:test")
include(":foundation:date:api")
include(":foundation:date:test")
include(":foundation:exceptions")
include(":entities")
include(":domains:user:data:api")
include(":domains:user:data:test")
include(":domains:user:domain:api")
include(":domains:user:domain:test")
include(":domains:user:controller")
include(":foundation:validator:api")
include(":foundation:validator:test")
include(":domains:invoice:data:api")
include(":domains:invoice:data:test")
include(":domains:invoice:domain:api")
include(":domains:invoice:domain:test")
include(":domains:invoice:controller")