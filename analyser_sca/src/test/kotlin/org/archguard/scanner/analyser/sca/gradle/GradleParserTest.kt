package org.archguard.scanner.analyser.sca.gradle

import org.archguard.context.DEP_SCOPE
import org.archguard.context.DeclFileTree
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class GradleParserTest {
    private val gradleSample = """
sourceCompatibility = 1.8
targetCompatibility = 1.8

dependencies {
    implementation "joda-time:joda-time:2.2"
    testImplementation "junit:junit:4.12"
}
""".trimIndent()

    @Test
    internal fun normal_match() {
        val declFileTree = DeclFileTree("archguard", "build.gradle", gradleSample)
        val depDecls = GradleParser().lookupSource(declFileTree)
        assertEquals(1, depDecls.size)

        val dependencies = depDecls[0].dependencies
        assertEquals(2, dependencies.size)
        assertEquals("joda-time:joda-time", dependencies[0].name)
        assertEquals("2.2", dependencies[0].version)

        assertEquals("junit", dependencies[1].group)
        assertEquals("junit", dependencies[1].artifact)
        assertEquals("4.12", dependencies[1].version)
        assertEquals(DEP_SCOPE.TEST, dependencies[1].scope)
    }

    @Test
    internal fun keyword_arg() {
        val declFileTree = DeclFileTree(
            "archguard", "build.gradle", """
dependencies {
    runtimeOnly(group = "org.springframework", name = "spring-core", version = "2.5")
}
        """.trimIndent()
        )
        val depDecls = GradleParser().lookupSource(declFileTree)
        assertEquals(1, depDecls.size)

        val dependencies = depDecls[0].dependencies
        assertEquals(1, dependencies.size)
        assertEquals(DEP_SCOPE.RUNTIME, dependencies[0].scope)
        assertEquals("org.springframework", dependencies[0].group)
        assertEquals("spring-core", dependencies[0].artifact)
        assertEquals("2.5", dependencies[0].version)
    }

    @Test
    internal fun dependency_set() {
        val declFileTree = DeclFileTree(
            "archguard", "build.gradle", """
dependencySet(group:'org.slf4j', version: '1.7.7') { 
    entry 'slf4j-api' 
}
        """.trimIndent()
        )
        val depDecls = GradleParser().lookupSource(declFileTree)
        assertEquals(1, depDecls.size)

        val dependencies = depDecls[0].dependencies
        assertEquals(1, dependencies.size)
        assertEquals("slf4j-api", dependencies[0].artifact)
        assertEquals("1.7.7", dependencies[0].version)
    }

    @Test
    internal fun dependency_set_multiple() {
        val declFileTree = DeclFileTree(
            "archguard", "build.gradle", """
dependencySet(group:'org.slf4j', version: '1.7.7') { 
    entry 'slf4j-api' 
    entry 'slf4j-simple'
}
        """.trimIndent()
        )
        val depDecls = GradleParser().lookupSource(declFileTree)
        assertEquals(1, depDecls.size)

        val dependencies = depDecls[0].dependencies
        assertEquals(2, dependencies.size)
        assertEquals("slf4j-api", dependencies[0].artifact)
        assertEquals("1.7.7", dependencies[0].version)

        assertEquals("slf4j-simple", dependencies[1].artifact)
        assertEquals("1.7.7", dependencies[1].version)
    }

    @Test
    internal fun error_single_line() {
        val declFileTree = DeclFileTree(
            "archguard", "build.gradle", """
libraries.junitJupiterApi = "org.junit.jupiter:junit-jupiter-api:4.4.0"
        """.trimIndent()
        )
        val depDecls = GradleParser().lookupSource(declFileTree)
        assertEquals(1, depDecls.size)

        val dependencies = depDecls[0].dependencies
        assertEquals(0, dependencies.size)
    }

    @Test
    internal fun single_line_with_scope() {
        val declFileTree = DeclFileTree(
            "archguard", "build.gradle.kts", """
dependencies {
    implementation(projects.metaAction)
    implementation(libs.kotlin.stdlib)

    implementation(libs.bundles.openai)

    implementation(libs.onnxruntime)
    implementation(libs.huggingface.tokenizers)
    implementation(libs.jtokkit)

    testImplementation(libs.bundles.test)
    testRuntimeOnly(libs.test.junit.engine)
}"""
        )

        val depDecls = GradleParser().lookupSource(declFileTree)
        assertEquals(1, depDecls.size)

        val dependencies = depDecls[0].dependencies
        assertEquals(7, dependencies.size)
        assertEquals("libs.kotlin.stdlib", dependencies[0].name)
    }

    @Test
    internal fun should_succes_parse_in_sub_projects() {
val declFileTree = DeclFileTree(
    "archguard", "build.gradle", """
subprojects {
    dependencies {
        implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
        implementation 'org.springframework.boot:spring-boot-starter-data-redis'
        implementation 'org.springframework.boot:spring-boot-starter-web'
        implementation 'org.springframework.boot:spring-boot-starter-validation'
        implementation 'org.flywaydb:flyway-core'
        implementation "org.springframework.boot:spring-boot-starter-data-redis"
        implementation 'org.apache.commons:commons-lang3:3.0'
        implementation 'org.springframework.security:spring-security-core:5.4.6'
        implementation 'mysql:mysql-connector-java'

        compileOnly 'org.projectlombok:lombok'
        developmentOnly 'org.springframework.boot:spring-boot-devtools'
        annotationProcessor 'org.springframework.boot:spring-boot-configuration-processor'
        annotationProcessor 'org.projectlombok:lombok'
        testImplementation 'org.springframework.boot:spring-boot-starter-test'
        testImplementation 'com.tngtech.archunit:archunit:0.23.1'
        testImplementation "ch.vorburger.mariaDB4j:mariaDB4j:2.4.0"
        testImplementation 'io.rest-assured:rest-assured:3.0.5'
        testImplementation 'org.dbunit:dbunit:2.7.0'
    }
}
"""
)

        val depDecls = GradleParser().lookupSource(declFileTree)
        assertEquals(1, depDecls.size)

        val dependencies = depDecls[0].dependencies
        assertEquals(18, dependencies.size)
    }
}
