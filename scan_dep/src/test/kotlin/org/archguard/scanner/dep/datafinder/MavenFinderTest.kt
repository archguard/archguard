package org.archguard.scanner.dep.datafinder

import org.archguard.scanner.dep.model.DEP_SCOPE
import org.archguard.scanner.dep.model.DeclFile
import org.archguard.scanner.dep.model.DepSource
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


internal class MavenFinderTest {
    private val sampleXml = """
        <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
          <modelVersion>4.0.0</modelVersion>
         
          <groupId>com.mycompany.app</groupId>
          <artifactId>my-app</artifactId>
          <version>1.0-SNAPSHOT</version>
         
          <properties>
            <maven.compiler.source>1.7</maven.compiler.source>
            <maven.compiler.target>1.7</maven.compiler.target>
          </properties>
         
          <dependencies>
            <dependency>
              <groupId>junit</groupId>
              <artifactId>junit</artifactId>
              <version>4.12</version>
              <scope>test</scope>
            </dependency>
          </dependencies>
        </project>
            """.trimIndent()

    @Test
    internal fun should_parse_xml_deps() {
        val declFile = DeclFile("archguard", "pom.xml", sampleXml)
        val lookupSource = MavenFinder().lookupSource(declFile)

        val dependencies = lookupSource[0].dependencies
        assertEquals(1, dependencies.size)
        assertEquals("junit:junit", dependencies[0].name)
        assertEquals("junit", dependencies[0].group[0])
        assertEquals("junit", dependencies[0].artifact)
        assertEquals("4.12", dependencies[0].version)
    }

    @Test
    internal fun should_parse_self_version() {
        val declFile = DeclFile("archguard", "pom.xml", sampleXml)
        val lookupSource = MavenFinder().lookupSource(declFile)

        assertEquals("com.mycompany.app:my-app", lookupSource[0].name)
        assertEquals("1.0-SNAPSHOT", lookupSource[0].version)
    }

    @Test
    internal fun should_parse_scope() {
        val declFile = DeclFile("archguard", "pom.xml", sampleXml)
        val lookupSource = MavenFinder().lookupSource(declFile)

        val dependencies = lookupSource[0].dependencies
        assertEquals(DEP_SCOPE.TEST, dependencies[0].scope)
    }
}