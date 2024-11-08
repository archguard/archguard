@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    base
    alias(libs.plugins.jvm)
    alias(libs.plugins.dokka)

    id("jacoco-report-aggregation")

    id("java-library")
    id("maven-publish")
    publishing
    signing
}

jacoco {
    toolVersion = "0.8.7"
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "jacoco")
    apply(plugin = "org.jetbrains.dokka")

    group = "org.archguard.scanner"
    version = "2.2.4"

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
    }

    tasks.getByName<Test>("test") {
        useJUnitPlatform()
    }

    tasks.withType<Test> {
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
}

// exclude `server` module publish
configure(
    allprojects
            - project(":server")
            // parent of modules
            - project(":architecture-as-code")
            // parent of modules
            - project(":rule-linter")
            // parent of modules
            - project(":analyser_sourcecode")
) {
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
                            id.set("ArchGuard")
                            name.set("ArchGuard Team")
                            email.set("h@phodal.com")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://github.com/archguard/archguard.git")
                        developerConnection.set("scm:git:ssh://github.com/archguard/archguard.git")
                        url.set("https://github.com/archguard/archguard/")
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
                        (if (project.findProperty("sonatypeUsername") != null) project.findProperty("sonatypeUsername") else {
                            System.getenv("MAVEN_USERNAME")
                        }).toString()
                    password =
                        (if (project.findProperty("sonatypePassword") != null) project.findProperty("sonatypePassword") else {
                            System.getenv("MAVEN_PASSWORD")
                        }).toString()
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

dependencies {
    jacocoAggregation(project(":server"))

    jacocoAggregation(project(":scanner_cli"))
    jacocoAggregation(project(":scanner_core"))

    jacocoAggregation(project(":analyser_sourcecode:lang_kotlin"))
    jacocoAggregation(project(":analyser_sourcecode:lang_java"))
    jacocoAggregation(project(":analyser_sourcecode:lang_typescript"))
    jacocoAggregation(project(":analyser_sourcecode:lang_python"))
    jacocoAggregation(project(":analyser_sourcecode:lang_golang"))
    jacocoAggregation(project(":analyser_sourcecode:lang_csharp"))
    jacocoAggregation(project(":analyser_sourcecode:lang_scala"))
    jacocoAggregation(project(":analyser_sourcecode:lang_c"))
    jacocoAggregation(project(":analyser_sourcecode:lang_cpp"))
    jacocoAggregation(project(":analyser_sourcecode:feat_apicalls"))
    jacocoAggregation(project(":analyser_sourcecode:feat_datamap"))
    jacocoAggregation(project(":analyser_git"))
    jacocoAggregation(project(":analyser_diff_changes"))
    jacocoAggregation(project(":analyser_sca"))
    jacocoAggregation(project(":analyser_architecture"))

    jacocoAggregation(project(":architecture-as-code:domain"))
    jacocoAggregation(project(":architecture-as-code:dsl"))
    jacocoAggregation(project(":architecture-as-code:repl-api"))

    jacocoAggregation(project(":rule-core"))
    jacocoAggregation(project(":rule-linter:rule-sql"))
    jacocoAggregation(project(":rule-linter:rule-test"))
    jacocoAggregation(project(":rule-linter:rule-webapi"))
    jacocoAggregation(project(":rule-linter:rule-code"))
    jacocoAggregation(project(":rule-linter:rule-layer"))
    jacocoAggregation(project(":doc-generator"))
}

reporting {
    reports {
        val jacocoRootReport by creating(JacocoCoverageReport::class) {
            testType.set(TestSuiteType.UNIT_TEST)
        }
    }
}

tasks.check {
    dependsOn(tasks.named<JacocoReport>("jacocoRootReport"))
}
