package org.archguard.scanner.analyser.backend.go

import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import chapi.domain.core.CodeImport
import org.archguard.context.ContainerDemand
import java.io.File

class GoProtobufConsumerAnalyser {
    private val dataStructs: List<CodeDataStruct>
    private val workspace: String
    private val parentSpace: String
    private val dsMap: Map<String, List<CodeDataStruct>>
    private var packageDsMap: Map<String, List<CodeDataStruct>>

    constructor(dataStructs: List<CodeDataStruct>, workspace: String) {
        this.parentSpace = File(workspace).parent
        dataStructs.forEach {
            it.FilePath = it.FilePath.replace(parentSpace, "").removePrefix("/")
        }
        this.dataStructs = dataStructs

        this.workspace = workspace
        this.dsMap = dataStructs.groupBy {
            it.FilePath.split(".").dropLast(1).joinToString("/")
        }

        this.packageDsMap = dataStructs.groupBy {
            it.FilePath.split("/").dropLast(1).joinToString("/")
        }
    }

    fun analysis(): List<ContainerDemand> {
        val singleMapping: MutableMap<String, List<String>> = analyzeAndMapCodePaths(dataStructs)
        val result: MutableList<ContainerDemand> = mutableListOf()

        singleMapping.filter { it.key.startsWith("RPC") }.map {
            val call = buildCallChain(singleMapping, it.key)
            result += ContainerDemand(
                source_caller = call.last(), call_routes = call, target_url = call.first(), target_http_method = "RPC"
            )
        }

        return result
    }

    /// if start with RPC. try to find the call routers => iterate the value, and value as key to find the next value
    /// if  classB.methodB call classA.methodA, RPC.someMethod, then the mapping should be
    //  - "RPC.someMethod" <-- classA.methodA
    /// - classA.methodA <-- classB.methodB
    /// But the result should be ["RPC.someMethod", classA.methodA, classB.methodB]
    fun buildCallChain(reverseMapping: MutableMap<String, List<String>>, target: String): List<String> {
        val result = mutableListOf<String>()
        val visited = mutableSetOf<String>()

        fun dfs(node: String) {
            if (visited.contains(node)) {
                return
            }

            visited.add(node)
            result.add(node)

            reverseMapping[node]?.forEach {
                dfs(it)
            }
        }

        dfs(target)
        return result
    }

    fun analyzeAndMapCodePaths(codeDataStructs: List<CodeDataStruct>): MutableMap<String, List<String>> {
        val currentDsMap = codeDataStructs.groupBy {
            it.NodeName
        }

        val importMap: MutableMap<String, CodeImport> = mutableMapOf()
        val targetToSource: MutableMap<String, List<String>> = mutableMapOf()

        codeDataStructs.forEach { ds ->
            val imports = ds.Imports
            imports.forEach { codeImport ->
                codeImport.UsageName.forEach {
                    importMap[it] = codeImport
                }
            }

            ds.Functions.forEach { function ->
                function.FunctionCalls.forEach { call ->
                    /// resolve NodeName in same package
                    val sourceStruct = call.NodeName.startsWith("Service") || call.NodeName.startsWith("Dao")
                    if (sourceStruct && call.NodeName.contains(".") && !call.NodeName.contains(".client")) {
                        val split = call.NodeName.split(".")
                        val struct = split.first()
                        val model = split.drop(1).joinToString(".")

                        val serviceStruct = currentDsMap[struct] ?: return@forEach
                        var serviceField =
                            serviceStruct.map { it.Fields.filter { field -> field.TypeValue == model } }.flatten()

                        /// lookup current dir to find the service
                        if (serviceField.isEmpty()) {
                            // get parent
                            val parent = ds.FilePath.split("/").dropLast(1).joinToString("/")
                            val underPackageDs = packageDsMap[parent]
                            underPackageDs?.forEach { parentDs ->
                                val parentStruct = parentDs.Fields.filter { field -> field.TypeValue == model }
                                if (parentStruct.isNotEmpty()) {
                                    serviceField = parentStruct
                                }
                            }
                        }

                        serviceField.forEach { codeField ->
                            val typeType = codeField.TypeType.removePrefix("*")
                            val importPath = typeType.split(".").first()

                            importPath.let {
                                val import = importMap[it]
                                val targetFile = dsMap[import?.Source ?: return@let]
                                val source = pathify(ds, function)
                                val callName = call.FunctionName

                                targetFile?.forEach { ds ->
                                    ds.Functions.forEach { targetFunction ->
                                        if (targetFunction.Name == callName) {
                                            targetToSource[pathify(ds, targetFunction)] = listOf(source)
                                        }
                                    }
                                }
                            }
                        }
                    } else if (call.NodeName == "Service.client" && call.FunctionName == "Call") {
                        /// or handle the net/rpc
                        if (imports.map { it.Source }
                                .contains("net/rpc") || imports.any { it.Source.contains("net/rpc") }) {
                            if (call.Parameters.size > 1) {
                                val secondCall = call.Parameters[1]
                                targetToSource[secondCall.TypeValue.removeSurrounding("\"")] =
                                    listOf(pathify(ds, function))
                            }
                        }
                    }
                }
            }
        }

        return targetToSource
    }

    private fun pathify(ds: CodeDataStruct, function: CodeFunction) =
        ds.FilePath.split(".").dropLast(1).joinToString("/") + "$" + ds.NodeName + "." + function.Name
}