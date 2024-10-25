package org.archguard.scanner.architecture.detect

import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeImport
import org.archguard.scanner.analyser.ArchitectureDetect
import org.archguard.architecture.code.ConnectorType
import org.archguard.architecture.detect.AppType
import org.archguard.architecture.detect.OutboundProtocol
import org.archguard.scanner.analyser.WorkspaceAnalyser
import org.archguard.scanner.analyser.techstack.FrameworkMarkup
import org.archguard.context.DependencyEntry
import org.archguard.context.PackageDependencies
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ArchitectureDetectTest {
    @Test
    internal fun basic_app_type() {
        val markup = FrameworkMarkup.byLanguage("Java")!!
        val dependencyEntry = DependencyEntry(
            name = "org.springframework.boot:spring-boot-starter-jdbc",
            group = "org.springframework.boot",
            artifact = "spring-boot-starter-jdbc",
            version = ""
        )
        val packageDependencies = PackageDependencies(
            name = "",
            version = "",
            packageManager = "Gradle",
            dependencies = listOf(dependencyEntry),
            ""
        )

        val potentialExecArch = ArchitectureDetect()
            .inferenceByDependencies(markup, listOf(packageDependencies))
        Assertions.assertEquals(AppType.Web, potentialExecArch.appTypes[0])
    }

    @Test
    internal fun basic_protocol() {
        val markup = FrameworkMarkup.byLanguage("Java")!!
        val dependencyEntry = DependencyEntry(
            name = "org.apache.dubbo:dubbo",
            group = "dubbo",
            artifact = "org.apache.dubbo:dubbo",
            version = ""
        )
        val packageDependencies = PackageDependencies(
            name = "",
            version = "",
            packageManager = "Gradle",
            dependencies = listOf(dependencyEntry),
            ""
        )

        val potentialExecArch = ArchitectureDetect()
            .inferenceByDependencies(markup, listOf(packageDependencies))
        Assertions.assertEquals(OutboundProtocol.RPC, potentialExecArch.protocols[0])
    }

    @Test
    fun exec_arch_from_source_code() {
        val struct = CodeDataStruct(
            Imports = listOf(CodeImport(Source = "java.io.File"))
        )
        val execArch = ArchitectureDetect().identPotential(WorkspaceAnalyser(listOf(struct)))

        assertEquals(1, execArch.connectorTypes.size)
        assertEquals(ConnectorType.FileIO, execArch.connectorTypes[0])
    }

    @Test
    internal fun core_stacks() {
        val markup = FrameworkMarkup.byLanguage("Java")!!
        val dependencyEntry = DependencyEntry(
            name = "org.springframework.boot:spring-boot-starter-jdbc",
            group = "org.springframework.boot",
            artifact = "spring-boot-starter-jdbc",
            version = ""
        )
        val packageDependencies = PackageDependencies(
            name = "",
            version = "",
            packageManager = "Gradle",
            dependencies = listOf(dependencyEntry),
            ""
        )

        val potentialExecArch = ArchitectureDetect()
            .inferenceByDependencies(markup, listOf(packageDependencies))
        Assertions.assertEquals("org.springframework.boot", potentialExecArch.coreStacks[0])
    }

}
