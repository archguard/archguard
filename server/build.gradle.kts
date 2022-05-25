val ktlint by configurations.creating

plugins {
    // springfox not support spring boot 2.6, see in https://github.com/springfox/springfox/issues/3462
    id("org.springframework.boot") version "2.5.10"

    // flyway 7.0 require spring .boot > 2.4
    id("org.flywaydb.flyway").version("7.15.0")
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("com.avast.gradle.docker-compose") version "0.15.2"

    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"

    id("org.jetbrains.dokka") version "1.6.21"

    jacoco

    // for maven publish
    id("java-library")
    id("maven-publish")
    publishing
    signing
}

group = "com.thoughtworks.archguard"
version = "2.0.0-alpha.7"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }
    mavenCentral()
    mavenLocal()
}

dependencies {
    // architecture as code part
    implementation("org.archguard.scanner:scanner_core:2.0.0-alpha.7")

    api(project(":architecture-as-code:dsl"))
    api(project(":architecture-as-code:repl-api"))

    ktlint("com.pinterest:ktlint:0.44.0") {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }

    // kotlin configs
    implementation(kotlin("stdlib"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    // kotlin reflection.
    implementation(kotlin("test"))
    implementation(kotlin("test-junit"))
    // kotlin coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")

    implementation("org.jdbi:jdbi3-core:3.28.0")
    implementation("org.jdbi:jdbi3-spring4:3.19.0")    // provide JdbiFactoryBean
    implementation("org.jdbi:jdbi3-kotlin-sqlobject:3.28.0")
    implementation("org.jdbi:jdbi3-kotlin:3.10.1")
    implementation("org.jdbi:jdbi3-testing:3.28.0")

    implementation("io.springfox:springfox-boot-starter:3.0.0")
    implementation("io.springfox:springfox-swagger-ui:3.0.0")

    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    // cache for overview api
    implementation("org.springframework.boot:spring-boot-starter-cache")

    implementation("org.nield:kotlinstatistics:0.3.0")
    implementation("com.alibaba:druid-spring-boot-starter:1.2.8")

    implementation("dom4j:dom4j:1.6.1")

    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    runtimeOnly("mysql:mysql-connector-java")

    implementation("org.flywaydb:flyway-core:7.15.0")
    testImplementation("io.mockk:mockk:1.12.3")
    testImplementation("com.github.database-rider:rider-spring:1.16.1")
    testImplementation("com.h2database:h2:1.4.200")
    testImplementation("org.flywaydb:flyway-core:6.5.7")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "jacoco")
    apply(plugin = "org.jetbrains.dokka")

    group = "org.archguard.scanner"
    version = "2.0.0-alpha.7"
    java.sourceCompatibility = JavaVersion.VERSION_1_8
    java.targetCompatibility = JavaVersion.VERSION_1_8

    repositories {
        mavenCentral()
        mavenLocal()
    }

    dependencies {
        implementation(kotlin("stdlib"))

        // log
        implementation("io.github.microutils:kotlin-logging:2.1.21")

        // test
        implementation(kotlin("test"))
        implementation(kotlin("test-junit"))

        testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    }
//
//    tasks.getByName<Test>("test") {
//        useJUnitPlatform()
//    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    tasks.test {
        finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
    }

    tasks.jacocoTestReport {
        dependsOn(tasks.test) // tests are required to run before generating the report
    }

    tasks.jacocoTestReport {
        reports {
            xml.required.set(true)
            csv.required.set(false)
            html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
        }
    }

    tasks.withType<JacocoReport> {
        afterEvaluate {
            classDirectories.setFrom(
                files(
                    classDirectories.files.map {
                        fileTree(it).apply {
                            exclude("dev.evolution")
                        }
                    }
                )
            )
        }
    }
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")
    apply(plugin = "publishing")

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                from(components["java"])
                versionMapping {
                    usage("java-api") {
                        fromResolutionOf("runtimeClasspath")
                    }
                    usage("java-runtime") {
                        fromResolutionResult()
                    }
                }
                pom {
                    name.set("ArchGuard")
                    description.set(" ArchGuard is a architecture governance tool which can analysis architecture in container, component, code level, create architecture fitness functions, and anaysis system dependencies.. ")
                    url.set("https://archguard.org/")
                    licenses {
                        license {
                            name.set("MIT")
                            url.set("https://raw.githubusercontent.com/archguard/archguard/master/LICENSE")
                        }
                    }
                    developers {
                        developer {
                            id.set("Modernizing")
                            name.set("Modernizing Team")
                            email.set("h@phodal.com")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://github.com/archguard/scanner.git")
                        developerConnection.set("scm:git:ssh://github.com/archguard/scanner.git")
                        url.set("https://github.com/archguard/scanner/")
                    }
                }
            }
        }

        repositories {
            maven {
                val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl

                credentials {
                    username =
                        (
                                if (project.findProperty("sonatypeUsername") != null) project.findProperty("sonatypeUsername") else System.getenv(
                                    "MAVEN_USERNAME"
                                )
                                ).toString()
                    password =
                        (
                                if (project.findProperty("sonatypePassword") != null) project.findProperty("sonatypePassword") else System.getenv(
                                    "MAVEN_PASSWORD"
                                )
                                ).toString()
                }
            }
        }
    }

    signing {
        sign(publishing.publications["mavenJava"])
    }

    java {
        withJavadocJar()
        withSourcesJar()
    }
}


configure<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension> {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:Hoxton.SR6")
    }
}
//
//tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
//    kotlinOptions {
//        freeCompilerArgs = listOf("-Xjsr305=strict")
//        jvmTarget = "1.8"
//    }
//}
//
//tasks.withType<Test> {
//    useJUnitPlatform()
//}
//
//tasks.jacocoTestReport {
//    dependsOn(tasks.test)
//}
//
//tasks.jacocoTestReport {
//    reports {
//        xml.required.set(true)
//        csv.required.set(false)
//        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
//    }
//}
//
//tasks.jacocoTestCoverageVerification {
//    violationRules {
//        rule {
//            limit {
//                counter = "LINE"
//                value = "COVEREDRATIO"
//                minimum = "0.25".toBigDecimal()
//            }
//        }
//
//        rule {
//            limit {
//                counter = "BRANCH"
//                value = "COVEREDRATIO"
//                minimum = "0.3".toBigDecimal()
//            }
//        }
//    }
//}
//
//tasks.jacocoTestCoverageVerification {
//    dependsOn(tasks.jacocoTestReport)
//}

val outputDir = "${project.buildDir}/reports/ktlint/"
val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))

val ktlintCheck by tasks.creating(JavaExec::class) {
    inputs.files(inputFiles)
    outputs.dir(outputDir)

    description = "Check Kotlin code style."
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    args = listOf("src/**/*.kt")
}

val ktlintFormat by tasks.creating(JavaExec::class) {
    inputs.files(inputFiles)
    outputs.dir(outputDir)

    description = "Fix Kotlin code style deviations."
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    args = listOf("-F", "src/**/*.kt")
}

val installGitHooks = task<Copy>("installGitHooks") {
    from(file("$projectDir/config/githooks/commit-msg"))
    into(file("$projectDir/.git/hooks"))
    fileMode = 493
}

tasks.check { dependsOn(installGitHooks) }

dockerCompose {
    isRequiredBy(project.tasks.bootRun)
    useComposeFiles.set(listOf("$projectDir/config/infrastructure/docker-compose.local.yml"))
    removeVolumes.set(false)
}
