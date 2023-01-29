@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    base
    java
    id("org.jetbrains.kotlin.jupyter.api") version "0.11.0-89-1"

    alias(libs.plugins.jvm)
    alias(libs.plugins.serialization)
}

group = "org.archguard.aaac"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
    maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev")
}

dependencies {
    api(project(":architecture-as-code:dsl"))
    implementation(kotlin("stdlib"))

    implementation(libs.serialization.json)

    implementation("org.jetbrains.kotlinx:kotlin-jupyter-api:0.11.0-89-1")
    implementation("org.jetbrains.kotlinx:kotlin-jupyter-kernel:0.11.0-89-1")

    // tips: don't add follow deps to project will cause issues
    compileOnly("org.jetbrains.kotlin:kotlin-scripting-jvm:1.6.21")

    implementation(libs.logback.classic)

    testImplementation(libs.bundles.test)
}
