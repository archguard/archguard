package org.archguard.architecture.detect

import chapi.domain.core.CodeDataStruct
import org.archguard.analyser.sca.model.PackageDependencies
import org.archguard.architecture.core.CodeStructureStyle
import org.archguard.architecture.core.ConnectorType
import org.archguard.architecture.core.Workspace

/**
 * 潜在的架构元素，后续需要根据这个继续分析
 */
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
        // 1. setup

        var execArch = PotentialExecArch()
        // 2. create exec arch
        val markup = FrameworkMarkup.byLanguage("Java")
        if (markup != null) {
            // todo: cache
            execArch = inferenceExecArchByDependencies(markup, workspace.projectDependencies)
        }

        // 3. update exec arch from call nodeName
        fillArchFromSourceCode(workspace, execArch)

        // 4. load all package name for layered architecture
        // todo: refactor
        val packages = workspace.dataStructs.map { it.Package }.toList()
        val layeredStyle = LayeredIdentify(packages).identify()
        execArch.layeredStyle = layeredStyle

        // 5. create concepts domain
        when (layeredStyle) {
            CodeStructureStyle.MVC -> {
                execArch.concepts = workspace.dataStructs.filter {
                    it.NodeName.contains("Entity")
                }
            }
            CodeStructureStyle.ModuleDDD,
            CodeStructureStyle.DDD -> {
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
     *  - identify appType : web, data, like
     *  - identify protocol: http, rpc
     *  - identify core stacks
     */
    fun inferenceExecArchByDependencies(markup: FrameworkMarkup, packageDeps: PackageDependencies): PotentialExecArch {
        val potentialExecArch = PotentialExecArch()
        val appTypeMap = markup.depAppTypeMap
        val protocols = markup.depProtocolMap
        val coreStacks = markup.coreStacks

        packageDeps.dependencies.forEach { depEntry ->
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

        return potentialExecArch
    }
}
