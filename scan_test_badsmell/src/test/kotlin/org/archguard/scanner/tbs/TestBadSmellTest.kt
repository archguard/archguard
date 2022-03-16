package org.archguard.scanner.tbs


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.nio.file.Paths

internal class TbsAnalyserTest {
    private fun getAbsolutePath(path: String): String {
        val resource = this.javaClass.classLoader.getResource(path)
        return Paths.get(resource!!.toURI()).toFile().absolutePath
    }

    @Test
    internal fun shouldIdentifyJavaEmptyTest() {
        val path = getAbsolutePath("tbs/usecases/EmptyTest.java")
        val results = TbsAnalyser().analysisByPath(path)

        assertEquals(results[0].FileName, "EmptyTest.java")
        assertEquals(results[0].Line, 8)
        assertEquals(results[0].Type, "EmptyTest")
    }

    @Test
    internal fun shouldIdentifyJavaIgnoreTest() {
        val path = getAbsolutePath("tbs/usecases/IgnoreTest.java")
        val results = TbsAnalyser().analysisByPath(path)

        assertEquals(results[0].Line, 7)
        assertEquals(results[0].Type, "IgnoreTest")
    }

    @Test
    internal fun shouldIdentifyJavaRedundantPrintTest() {
        val path = getAbsolutePath("tbs/usecases/RedundantPrintTest.java")
        val results = TbsAnalyser().analysisByPath(path)

        assertEquals(results[0].Line, 9)
        assertEquals(results[0].Type, "RedundantPrintTest")
    }

    @Test
    internal fun shouldIdentifyJavaSleepyTest() {
        val path = getAbsolutePath("tbs/usecases/SleepyTest.java")
        val results = TbsAnalyser().analysisByPath(path)

        assertEquals(results[0].Line, 8)
        assertEquals(results[0].Type, "SleepyTest")
    }

    @Test
    internal fun shouldIdentifyRedundantAssertionTest() {
        val path = getAbsolutePath("tbs/usecases/RedundantAssertionTest.java")
        val results = TbsAnalyser().analysisByPath(path)

        assertEquals(results[0].Line, 14)
        assertEquals(results[0].Type, "RedundantAssertionTest")
    }

    @Test
    internal fun shouldIdentifyUnknownTest() {
        val path = getAbsolutePath("tbs/usecases/UnknownTest.java")
        val results = TbsAnalyser().analysisByPath(path)

        assertEquals(results[0].Line, 7)
        assertEquals(results[0].Type, "EmptyTest")
        assertEquals(results[1].Line, 7)
        assertEquals(results[1].Type, "UnknownTest")
    }

    @Test
    internal fun shouldIdentifyDuplicateAssertTest() {
        val path = getAbsolutePath("tbs/usecases/DuplicateAssertTest.java")
        val results = TbsAnalyser().analysisByPath(path)

        assertEquals(results[0].Line, 9)
        assertEquals(results[0].Type, "DuplicateAssertTest")
    }

    @Test
    internal fun shouldReturnEmptyWhenIsCreator() {
        val path = getAbsolutePath("regression/CreatorNotUnknownTest.java")
        val results = TbsAnalyser().analysisByPath(path)

        assertEquals(results.size, 0)
    }

    @Test
    internal fun shouldReturnEmptyWhenCallAssertInClassTests() {
        val path = getAbsolutePath("regression/CallAssertInClassTests.java")
        val results = TbsAnalyser().analysisByPath(path)

        assertEquals(results.size, 0)
    }

    @Test
    internal fun shouldReturnEmptyWhenCall() {
        val path = getAbsolutePath("regression/EnvironmentSystemIntegrationTests.java")
        val results = TbsAnalyser().analysisByPath(path)

        assertEquals(results.size, 0)
    }

    @Test
    internal fun shouldReturnMultipleResults() {
        val path = getAbsolutePath("regression/I18NTest.java")
        val results = TbsAnalyser().analysisByPath(path)

        assertEquals(results.size, 4)
    }
}
