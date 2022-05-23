rootProject.name = "archguard"

include(":")
include(":model")

pluginManagement {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
        gradlePluginPortal()
    }
}
