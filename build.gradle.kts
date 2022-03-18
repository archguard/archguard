plugins {
    base
    java
    id("jacoco-report-aggregation")
}

allprojects {
    group = "com.thoughtworks.archguard"

    repositories {
        mavenCentral()
        mavenLocal()
        maven {
            url = uri("https://repo.maven.apache.org/maven2/")
        }
    }
}

jacoco {
    toolVersion = "0.8.7"
}

subprojects {
    apply(plugin = "jacoco")
    apply(plugin = "java")

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
            classDirectories.setFrom(files(classDirectories.files.map {
                fileTree(it).apply {
                    exclude("chapi/ast/antlr")
                }
            }))
        }
    }
}

dependencies {
    jacocoAggregation(project(":common"))
    jacocoAggregation(project(":scan_git"))
    jacocoAggregation(project(":scan_jacoco"))
    jacocoAggregation(project(":scan_sourcecode"))
    jacocoAggregation(project(":scan_mysql"))
    jacocoAggregation(project(":scan_test_badsmell"))
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
