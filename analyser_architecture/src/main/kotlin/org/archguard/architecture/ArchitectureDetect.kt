package org.archguard.architecture

import chapi.domain.core.CodeDataStruct
import org.archguard.analyser.sca.model.PackageDependencies
import org.archguard.architecture.core.CodeStructureStyle.CLEAN
import org.archguard.architecture.core.CodeStructureStyle.DDD
import org.archguard.architecture.core.CodeStructureStyle.MVC
import org.archguard.architecture.core.CodeStructureStyle.ModuleDDD
import org.archguard.architecture.core.CodeStructureStyle.UNKNOWN
import org.archguard.architecture.core.ExecutionArchitecture
import org.archguard.architecture.core.Workspace
import org.archguard.architecture.detect.FrameworkMarkup
import org.archguard.architecture.detect.LayeredIdentify

// 可能的执行架构元素
data class PotentialExecArch(
    var protocols: List<String> = listOf(),
    var appTypes: List<String> = listOf(),
    var stacks: List<String> = listOf()
)

class ArchitectureDetect() {
    fun ident(workspace: Workspace) {
        // 1. setup

        var execArch = PotentialExecArch()
        // 2. create exec arch
        val markup = FrameworkMarkup.byLanguage("Java")
        if (markup != null) {
            execArch = inferenceExecArchByDependencies(markup, workspace.projectDependencies)
        }

        // 3. update exec arch from call nodeName
        val callSources = workspace.dataStructs.flatMap { ds -> ds.FunctionCalls.map { it.NodeName }.toList() }.toList()
        if (callSources.contains("ProcessBuilder")) {
            // todo: add for process builder
            execArch.appTypes += "Process"
        }

        // 4. load all package name for layered architecture
        val packages = workspace.dataStructs.map { it.Package }.toList()
        val layeredStyle = LayeredIdentify(packages).identify()

        // 5. create concepts domain
        var concepts: List<CodeDataStruct> = listOf()
        when (layeredStyle) {
            MVC -> {}
            ModuleDDD,
            DDD -> {
                concepts = workspace.dataStructs.filter {
                    it.Package.contains("domain") && !it.NodeName.contains("Factory")
                }
            }
            CLEAN -> {}
            UNKNOWN -> {}
        }
    }

    // create execute architecture by dependencies
    //  - identify appType : web, data, like
    //  - identify protocol: http, rpc
    fun inferenceExecArchByDependencies(markup: FrameworkMarkup, packageDeps: PackageDependencies): PotentialExecArch {
        val potentialExecArch = PotentialExecArch()
        val appTypeMap = markup.depAppTypeMap
        val protocols = markup.depProtocolMap

        packageDeps.dependencies.forEach { depEntry ->
            appTypeMap.forEach {
                if (depEntry.name.startsWith(it.key)) {
                    potentialExecArch.appTypes += it.value
                }
            }
            protocols.forEach {
                if (depEntry.name.startsWith(it.key)) {
                    potentialExecArch.protocols += it.value
                }
            }
        }

        return potentialExecArch
    }
}