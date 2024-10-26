package org.archguard.scanner.analyser

import chapi.domain.core.CodeDataStruct
import org.archguard.context.PackageDependencies
import org.archguard.architecture.code.CodeStructureStyle
import org.archguard.architecture.code.ConnectorType
import org.archguard.architecture.detect.AppType
import org.archguard.architecture.detect.OutboundProtocol
import org.archguard.architecture.detect.PotentialExecArch
import org.archguard.architecture.graph.TreeNode
import org.archguard.scanner.analyser.layered.LayeredIdentify
import org.archguard.scanner.analyser.techstack.FrameworkMarkup

/**
 * The `ArchitectureDetect` class is responsible for identifying the potential execution architecture based on the given
 * workspace. It provides methods to infer the execution architecture by analyzing the project's language, dependencies,
 * source code, and package structure.
 */
class ArchitectureDetect {
    /**
     * Identifies the potential execution architecture based on the given workspace.
     *
     * @param workspaceAnalyser The workspace containing the project information.
     * @return The `PotentialExecArch` object representing the identified execution architecture.
     */
    fun identPotential(workspaceAnalyser: WorkspaceAnalyser): PotentialExecArch {
        // 1. create exec arch
        var execArch = PotentialExecArch()

        // Identify execution architecture based on the project's language
        val byLanguage = FrameworkMarkup.byLanguage(workspaceAnalyser.language)
        byLanguage?.also {
            execArch = inferenceByDependencies(it, workspaceAnalyser.projectDependencies)
        }

        // 2. update exec arch from call nodeName
        fillArchFromSourceCode(workspaceAnalyser, execArch)

        // 3. load all package name for layered architecture
        val packages = workspaceAnalyser.dataStructs.map { it.Package }.toList()
        val layeredStyle = LayeredIdentify(packages).identify()
        execArch.layeredStyle = layeredStyle

        // 4. create packages structure
        execArch.physicalStructure = TreeNode.create(packages)

        // 5. create concepts domain based on the identified layered architecture style
        execArch.concepts = buildConcepts(layeredStyle, workspaceAnalyser)

        // 6. keep all ds for second steps
        execArch.dataStructures = workspaceAnalyser.dataStructs

        return execArch
    }

    /// protobuf end suffix format
    private val protobufDtoEnds = listOf(
        "Request", "Response", "DTO", "VO", "Model", "Req", "Resp", "Reply"
    )

    /**
     * Builds a list of code data structures based on the given layered style and workspace.
     *
     * @param layeredStyle The layered style of the code structure.
     * @param workspaceAnalyser The workspace containing the code data structures.
     * @return A list of code data structures that match the given layered style and workspace.
     */
    private fun buildConcepts(
        layeredStyle: CodeStructureStyle,
        workspaceAnalyser: WorkspaceAnalyser
    ): List<CodeDataStruct> {
        // check workspace.dataStructs language if is idl
        val codeDataStructs = when (layeredStyle) {
            CodeStructureStyle.MVC -> {
                workspaceAnalyser.dataStructs.filter { dataStruct ->
                    dataStruct.Annotations.any { it.Name == "Entity" }
                }
            }

            CodeStructureStyle.ModuleDDD, CodeStructureStyle.DDD -> {
                workspaceAnalyser.dataStructs.filter {
                    it.Package.contains("domain") && !it.NodeName.contains("Factory")
                }
            }

            CodeStructureStyle.CLEAN -> {
                listOf()
            }

            CodeStructureStyle.UNKNOWN -> {
                listOf()
            }
        }.toMutableList()

        /// some special case
        val protoBufs = workspaceAnalyser.dataStructs.mapNotNull { dataStruct ->
            if (dataStruct.FilePath.endsWith(".proto")) {
                protobufDtoEnds.forEach {
                    if (dataStruct.NodeName.endsWith(it)) {
                        return@mapNotNull null
                    }
                }

                // if the model is anemic, it needs a service
//                if (dataStruct.NodeName.endsWith("Service")) {
//                    return@mapNotNull null
//                }

//                // skip for Functions is not empty
//                if (dataStruct.Functions.isNotEmpty()) {
//                    return@mapNotNull null
//                }

                dataStruct
            } else {
                null
            }
        }

        return codeDataStructs + protoBufs
    }

    /**
     * Fills the execution architecture information from the source code of the workspace.
     *
     * @param workspaceAnalyser The workspace containing the project information.
     * @param execArch The `PotentialExecArch` object to be updated.
     */
    private fun fillArchFromSourceCode(workspaceAnalyser: WorkspaceAnalyser, execArch: PotentialExecArch) {
        workspaceAnalyser.dataStructs.forEach { struct ->
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
     * Infers the execution architecture based on the project's dependencies and the provided framework markup.
     *
     * @param markup The framework markup containing the mapping of dependencies to architecture elements.
     * @param packageDeps The list of package dependencies in the project.
     * @return The `PotentialExecArch` object representing the inferred execution architecture.
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
                        potentialExecArch.appTypes += AppType.fromString(it.value)
                    }
                }

                // protocols
                protocols.forEach {
                    if (depEntry.name.startsWith(it.key)) {
                        potentialExecArch.protocols += OutboundProtocol.fromString(it.value)
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
