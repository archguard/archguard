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

        // 2. identify web, data, like
        val markup = FrameworkMarkup.byLanguage("Java")
        if (markup != null) {
            detectAppType(markup, workspace.projectDependencies)
        }

        // 3. analysis protocol

        // 4. load all package name for layered architecture
    }

    fun detectAppType(markup: FrameworkMarkup, dependencies: PackageDependencies): PotentialExecArch {
        val potentialExecArch = PotentialExecArch()
        val appTypeMap = markup.depToAppType()

        dependencies.dependencies.forEach { depEntry ->
            appTypeMap.forEach {
                if(depEntry.name.startsWith(it.key)) {
                    potentialExecArch.appTypes += it.value
                }
            }
        }

        return potentialExecArch
    }
}