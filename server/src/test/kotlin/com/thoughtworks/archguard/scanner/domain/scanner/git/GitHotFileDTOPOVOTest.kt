package com.thoughtworks.archguard.scanner.domain.scanner.git

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class GitHotFileDTOPOVOTest {
    @Test
    internal fun shouldReturnFalseWhenCheckIfJVMClassGivenNonJVMFile() {
        val gitHotFile = GitHotFileVO("README.MD", 10)

        assertFalse(gitHotFile.isJVMClass())
    }

    @Test
    internal fun shouldReturnTrueWhenCheckIfJVMClassGivenJavaFile() {
        val gitHotFile = GitHotFileVO("src/main/java/com/qicaisheng/parkinglot/HTMLReportVisitor.java", 10)

        assertTrue(gitHotFile.isJVMClass())
    }

    @Test
    internal fun shouldReturnTrueWhenCheckIfJVMClassGivenKotlinFile() {
        val gitHotFile = GitHotFileVO("src/main/kotlin/com/qicaisheng/parkinglot/HTMLReportVisitor.kt", 10)

        assertTrue(gitHotFile.isJVMClass())
    }

    @Test
    internal fun shouldGetClassNameGivenJavaFile() {
        val gitHotFile = GitHotFileVO("src/main/java/com/qicaisheng/parkinglot/HTMLReportVisitor.java", 10)

        assertEquals("com.qicaisheng.parkinglot.HTMLReportVisitor", gitHotFile.className())
    }

    @Test
    internal fun shouldGetClassNameGivenKotlinFile() {
        val gitHotFile = GitHotFileVO("src/main/kotlin/com/qicaisheng/parkinglot/HTMLReportVisitor.kt", 10)

        assertEquals("com.qicaisheng.parkinglot.HTMLReportVisitor", gitHotFile.className())
    }

    @Test
    internal fun shouldGetNullClassNameGivenNonJVMFile() {
        val gitHotFile = GitHotFileVO("README.MD", 10)

        assertEquals(null, gitHotFile.className())
    }

    @Test
    internal fun shouldGetNullModuleNameGivenJVMFileInSrcTopDirection() {
        val gitHotFile = GitHotFileVO("src/main/kotlin/com/qicaisheng/parkinglot/HTMLReportVisitor.kt", 10)

        assertEquals("root", gitHotFile.moduleName())
    }

    @Test
    internal fun shouldGetModuleNameGivenJVMFileInModuleSrcTopDirection() {
        val gitHotFile = GitHotFileVO("dubbo-samples-zookeeper/src/main/java/org/apache/dubbo/samples/action/GreetingServiceConsumer.java", 10)

        assertEquals("dubbo-samples-zookeeper", gitHotFile.moduleName())
    }

    @Test
    internal fun shouldGetModuleNameGivenJVMFileNotInModuleSrcTopDirection() {
        val gitHotFile = GitHotFileVO("dubbo/dubbo-samples-zookeeper/src/main/java/org/apache/dubbo/samples/action/GreetingServiceConsumer.java", 10)

        assertEquals("dubbo-samples-zookeeper", gitHotFile.moduleName())
    }

    @Test
    internal fun shouldGetModuleNameGivenJVMFileNotInModuleSrcTopDirection2() {
        val gitHotFile = GitHotFileVO("dir/dubbo/dubbo-samples-zookeeper/src/main/java/org/apache/dubbo/samples/action/GreetingServiceConsumer.java", 10)

        assertEquals("dubbo-samples-zookeeper", gitHotFile.moduleName())
    }
}
