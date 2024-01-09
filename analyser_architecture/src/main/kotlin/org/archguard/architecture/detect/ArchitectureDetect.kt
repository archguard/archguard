package org.archguard.architecture.detect

import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.Serializable
import org.archguard.scanner.core.sca.PackageDependencies
import org.archguard.architecture.core.CodeStructureStyle
import org.archguard.architecture.core.ConnectorType
import org.archguard.architecture.core.Workspace
import org.archguard.architecture.layered.LayeredIdentify
import org.archguard.architecture.techstack.FrameworkMarkup

/**
 * 潜在的架构元素，后续需要根据这个继续分析
 */
@Serializable
data class PotentialExecArch(
    var layeredStyle: CodeStructureStyle = CodeStructureStyle.UNKNOWN,
    var protocols: List<String> = listOf(),
    var appTypes: List<String> = listOf(),
    var connectorTypes: List<ConnectorType> = listOf(),
    var coreStacks: List<String> = listOf(),
    var concepts: List<CodeDataStruct> = listOf()
)

class ArchitectureDetect {
    fun identPotential(workspace: Workspace): PotentialExecArch {
        // 1. create exec arch
        var execArch = PotentialExecArch()

        val byLanguage = FrameworkMarkup.byLanguage(workspace.language)
        byLanguage?.also {
            execArch = inferenceByDependencies(it, workspace.projectDependencies)
        }

        // 2. update exec arch from call nodeName
        fillArchFromSourceCode(workspace, execArch)

        // 3. load all package name for layered architecture
        val packages = workspace.dataStructs.map { it.Package }.toList()
        val layeredStyle = LayeredIdentify(packages).identify()
        execArch.layeredStyle = layeredStyle

        // 4. create concepts domain
        when (layeredStyle) {
            CodeStructureStyle.MVC -> {
                execArch.concepts = workspace.dataStructs.filter { dataStruct ->
                    dataStruct.Annotations.any { it.Name == "Entity" }
                }
            }

            CodeStructureStyle.ModuleDDD, CodeStructureStyle.DDD -> {
                execArch.concepts = workspace.dataStructs.filter {
                    it.Package.contains("domain") && !it.NodeName.contains("Factory")
                }
            }

            CodeStructureStyle.CLEAN -> {}
            CodeStructureStyle.UNKNOWN -> {}
        }

        return execArch
    }

    private fun fillArchFromSourceCode(workspace: Workspace, execArch: PotentialExecArch) {
        workspace.dataStructs.forEach { struct ->
            struct.Imports.forEach {
                if (it.Source == "java.io.File") {
                    execArch.connectorTypes += ConnectorType.FileIO
                }
            }

            struct.FunctionCalls.forEach {
                if (it.NodeName == "ProcessBuilder") {
                    execArch.connectorTypes += ConnectorType.Process
                }
            }
        }
    }

    /**
     *  create execute architecture by dependencies
     *  - identify the channel type: web, data, like
     *  - identify protocol: http, rpc
     *  - identify core stacks
     */
    fun inferenceByDependencies(markup: FrameworkMarkup, packageDeps: List<PackageDependencies>): PotentialExecArch {
        val potentialExecArch = PotentialExecArch()
        val appTypeMap = markup.depAppTypeMap
        val protocols = markup.depProtocolMap
        val coreStacks = markup.coreStacks

        packageDeps.map { dependencies ->
            dependencies.dependencies.forEach { depEntry ->
                // app types
                appTypeMap.forEach {
                    if (depEntry.name.startsWith(it.key)) {
                        potentialExecArch.appTypes += it.value
                    }
                }

                // protocols
                protocols.forEach {
                    if (depEntry.name.startsWith(it.key)) {
                        potentialExecArch.protocols += it.value
                    }
                }

                // core stacks
                coreStacks.forEach {
                    if (depEntry.name.startsWith(it)) {
                        potentialExecArch.coreStacks += it
                    }
                }
            }
        }

        return potentialExecArch.copy(
            appTypes = potentialExecArch.appTypes.distinct(),
            protocols = potentialExecArch.protocols.distinct(),
            coreStacks = potentialExecArch.coreStacks.distinct()
        )
    }
}
