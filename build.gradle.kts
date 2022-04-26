plugins {
    base
    java
    id("jacoco-report-aggregation")
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    id("org.jetbrains.dokka") version "1.6.20"
}

jacoco {
    toolVersion = "0.8.7"
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "jacoco")
    apply(plugin = "org.jetbrains.dokka")

    group = "com.thoughtworks.archguard"
    version = "1.7.0"
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

    tasks.getByName<Test>("test") {
        useJUnitPlatform()
    }

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

dependencies {
    jacocoAggregation(project(":diff_changes"))

    jacocoAggregation(project(":"))

    jacocoAggregation(project(":common_code_repository"))

    jacocoAggregation(project(":rule_core"))
    jacocoAggregation(project(":linter:rule_sql"))
    jacocoAggregation(project(":linter:rule_test_code"))
    jacocoAggregation(project(":linter:rule_webapi"))
    jacocoAggregation(project(":linter:rule_code"))
    jacocoAggregation(project(":linter:rule_template"))

    jacocoAggregation(project(":scanner_cli"))
    jacocoAggregation(project(":analyser_sourcecode:lang_kotlin"))
    jacocoAggregation(project(":analyser_sourcecode:lang_java"))
    jacocoAggregation(project(":analyser_sourcecode:lang_typescript"))
    jacocoAggregation(project(":analyser_sourcecode:lang_python"))
    jacocoAggregation(project(":analyser_sourcecode:lang_golang"))
    jacocoAggregation(project(":analyser_sourcecode:lang_csharp"))
    jacocoAggregation(project(":analyser_sourcecode:lang_scala"))
    jacocoAggregation(project(":analyser_sourcecode:feat_apicalls"))
    jacocoAggregation(project(":analyser_sourcecode:feat_datamap"))

    jacocoAggregation(project(":analyser_sca"))
    jacocoAggregation(project(":analyser_architecture"))

    jacocoAggregation(project(":scan_git"))
    jacocoAggregation(project(":scan_jacoco"))
    // jacocoAggregation(project(":scan_sourcecode"))
    jacocoAggregation(project(":scan_sql"))
    jacocoAggregation(project(":scan_test_badsmell"))
    jacocoAggregation(project(":scan_bytecode"))
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
