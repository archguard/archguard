package org.archguard.architecture

import org.archguard.analyser.sca.model.PackageDependencies
import org.archguard.architecture.core.Workspace
import org.archguard.architecture.detect.FrameworkMarkup

// 可能的执行架构元素
data class PotentialExecArch(
    var protocols: List<String> = listOf(),
    var appTypes: List<String> = listOf(),
    var stacks: List<String> = listOf()
)

class ArchitectureDetect() {
    fun ident(workspace: Workspace) {
        // 0. setup

        // 1. load project dependencies

        val markup = FrameworkMarkup.byLanguage("Java")
        if (markup != null) {
            inferenceExecArchByDependencies(markup, workspace.projectDependencies)
        }

        // 4. load all package name for layered architecture
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
                if(depEntry.name.startsWith(it.key)) {
                    potentialExecArch.appTypes += it.value
                }
            }
            protocols.forEach {
                if(depEntry.name.startsWith(it.key)) {
                    potentialExecArch.protocols += it.value
                }
            }
        }

        return potentialExecArch
    }
}