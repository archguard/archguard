rootProject.name = "archguard"

include(":")

/**
 * architecture as code repos
 */

include(":architecture-as-code:domain")
include(":architecture-as-code:dsl")
include(":architecture-as-code:repl-api")

pluginManagement {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
        gradlePluginPortal()
    }
}
