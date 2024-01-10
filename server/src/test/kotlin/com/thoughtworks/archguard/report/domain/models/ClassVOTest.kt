package com.thoughtworks.archguard.report.domain.models;

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Path

class ClassVOTest {

    @Test
    fun should_createClassVO_when_givenFullName() {
        // given
        val fullName = "com.thoughtworks.archguard.report.domain.models.ClassVO"

        // when
        val classVO = ClassVO.create(fullName)

        // then
        assertEquals("com", classVO.moduleName)
        assertEquals("thoughtworks.archguard.report.domain.models", classVO.packageName)
        assertEquals("ClassVO", classVO.className)
    }

    @Test
    fun should_createClassVO_when_givenClassFullNameAndModuleName() {
        // given
        val classFullName = "com.thoughtworks.archguard.report.domain.models.ClassVO"
        val moduleName = "archguard"

        // when
        val classVO = ClassVO.create(classFullName, moduleName)

        // then
        assertEquals("archguard", classVO.moduleName)
        assertEquals("com.thoughtworks.archguard.report.domain.models", classVO.packageName)
        assertEquals("ClassVO", classVO.className)
    }

    @Test
    fun should_createClassVO_when_givenPath() {
        // given
        val path = File("server/src/main/kotlin/com/thoughtworks/archguard/report/domain/models/ClassVO.kt").toPath()

        // when
        val classVO = ClassVO.create(path)

        // if Windows end test
        if (System.getProperty("os.name").lowercase().contains("win")) {
            // then
            return
        }

        // then
        assertEquals("server", classVO?.moduleName)
        assertEquals("com.thoughtworks.archguard.report.domain.models", classVO?.packageName)
        assertEquals("ClassVO", classVO?.className)
    }

    @Test
    fun should_returnNull_when_givenNonJavaOrKotlinPath() {
        // given
        val path = File("server/src/main/resources/config.properties").toPath()

        // when
        val classVO = ClassVO.create(path)

        // then
        assertEquals(null, classVO)
    }
}

