plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
}

group = "com.thoughtworks.aac"

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0-M1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter:5.9.0-M1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}