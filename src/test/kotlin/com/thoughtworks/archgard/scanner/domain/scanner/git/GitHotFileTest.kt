package com.thoughtworks.archgard.scanner.domain.scanner.git

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class GitHotFileTest {
    @Test
    internal fun shouldReturnFalseWhenCheckIfJVMClassGivenNonJVMFile() {
        val gitHotFile = GitHotFile(1, "README.MD", 10)

        assertFalse(gitHotFile.isJVMClass())
    }

    @Test
    internal fun shouldReturnTrueWhenCheckIfJVMClassGivenJavaFile() {
        val gitHotFile = GitHotFile(1, "src/main/java/com/qicaisheng/parkinglot/HTMLReportVisitor.java", 10)
        
        assertTrue(gitHotFile.isJVMClass())
    }

    @Test
    internal fun shouldReturnTrueWhenCheckIfJVMClassGivenKotlinFile() {
        val gitHotFile = GitHotFile(1, "src/main/kotlin/com/qicaisheng/parkinglot/HTMLReportVisitor.kt", 10)

        assertTrue(gitHotFile.isJVMClass())
    }

    @Test
    internal fun shouldGetClassNameGivenJavaFile() {
        val gitHotFile = GitHotFile(1, "src/main/java/com/qicaisheng/parkinglot/HTMLReportVisitor.java", 10)

        assertEquals("com.qicaisheng.parkinglot.HTMLReportVisitor", gitHotFile.className())
    }

    @Test
    internal fun shouldGetClassNameGivenKotlinFile() {
        val gitHotFile = GitHotFile(1, "src/main/kotlin/com/qicaisheng/parkinglot/HTMLReportVisitor.kt", 10)

        assertEquals("com.qicaisheng.parkinglot.HTMLReportVisitor", gitHotFile.className())
    }
}