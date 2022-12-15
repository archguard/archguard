plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
//    id("org.archguard.scanner")
}

dependencies {
    api(project(":rule-core"))

    api("com.phodal.chapi:chapi-domain:2.0.0-beta.9")
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")

    testImplementation("io.mockk:mockk:1.12.3")
    testImplementation("org.assertj:assertj-core:3.22.0")
}
//
//archguard {
//    serverUrl = "http://localhost:8088"
//    language = "kotlin"
//    features += "apicalls"
//    path += "src/main"
//    output += "http"
//
//    slots {
//        create("slot") {
//            identifier = "rule"
//            host = "https://github.com/archguard/archguard/releases/download/v2.0.0-alpha.17"
//            version = "2.0.0-alpha.17"
//            jar = "rule-webapi-2.0.0-alpha.17-all.jar"
//            className = "org.archguard.linter.rule.webapi.WebApiRuleSlot"
//            slotType = "rule"
//        }
//    }
//}
