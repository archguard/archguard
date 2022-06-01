plugins {
    application

    kotlin("jvm") version "1.6.21"

    id("com.github.johnrengelman.shadow") version "7.0.0"
}

dependencies {
    api(project(":rule-core"))

    implementation("com.github.jsqlparser:jsqlparser:4.4")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.21")
}


application {
    mainClass.set("org.archguard.rule.RulerKt")
}

tasks {
    shadowJar {
        dependencies {
            exclude(dependency("org.jetbrains.kotlin:.*:.*"))
            exclude(dependency("org.jetbrains.kotlinx:.*:.*"))
        }
        minimize()
    }
}

