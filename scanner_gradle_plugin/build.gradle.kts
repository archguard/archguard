repositories {
    gradlePluginPortal()
    mavenCentral()
}

plugins {
    kotlin("jvm") version "1.6.21"
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "1.1.0"
}

dependencies {
    implementation("org.archguard.scanner:scanner_core:2.0.0-beta.4")
    implementation("org.archguard.scanner:scanner_cli:2.0.0-beta.4")

    implementation(kotlin("stdlib"))
    implementation(gradleApi())
}

gradlePlugin {
    plugins {
        create("org.archguard.scanner") {
            id = "org.archguard.scanner"
            implementationClass = "org.archguard.scanner.gradle.plugin.ArchguardPlugin"
            version = "0.0.1"
            displayName = "ArchGuard"
        }
    }
}

pluginBundle {
    website = "https://github.com/archguard/archguard"
    vcsUrl = "https://github.com/archguard/archguard"
    description = "ArchGuard"
    tags = listOf("architecture", "archguard", "guard", "lint")
}

tasks.create("setupPluginUploadFromEnvironment") {
    doLast {
        val key = System.getenv("GRADLE_PUBLISH_KEY")
        val secret = System.getenv("GRADLE_PUBLISH_SECRET")

        if (key == null || secret == null) {
            throw GradleException("gradlePublishKey and/or gradlePublishSecret are not defined environment variables")
        }

        System.setProperty("gradle.publish.key", key)
        System.setProperty("gradle.publish.secret", secret)
    }
}

