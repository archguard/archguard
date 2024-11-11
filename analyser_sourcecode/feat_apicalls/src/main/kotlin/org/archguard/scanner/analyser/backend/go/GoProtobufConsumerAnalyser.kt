package org.archguard.scanner.analyser.backend.go

import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import chapi.domain.core.CodeImport
import org.archguard.context.ContainerDemand
import java.io.File

class GoProtobufConsumerAnalyser(private val dataStructs: List<CodeDataStruct>, val workspace: String) {
    private val parentSpace: String = File(workspace).parent

    private val allDs: Map<String, List<CodeDataStruct>> = dataStructs
        .map {
            it.FilePath = it.FilePath.replace(parentSpace, "").removePrefix("/")
            it
        }
        .groupBy {
            it.FilePath.split(".").dropLast(1).joinToString("/")
        }

    fun analysis(): List<ContainerDemand> {
        val singleMapping: MutableMap<String, List<String>> = dataStructs
            .map { analyzeAndMapCodePaths(listOf(it)) }.reduce { acc, map ->
                acc.putAll(map)
                acc
            }

        val result: MutableList<ContainerDemand> = mutableListOf()

        singleMapping.filter { it.key.startsWith("RPC") }.map {
            val call = buildCallChain(singleMapping, it.key)
            result += ContainerDemand(
                source_caller = call.last(),
                call_routes = call,
                target_url = call.first(),
                target_http_method = "RPC"
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
        val imports = codeDataStructs.first().Imports
        imports.forEach { codeImport ->
            codeImport.UsageName.forEach {
                importMap[it] = codeImport
            }
        }

        val targetToSource: MutableMap<String, List<String>> = mutableMapOf()
        codeDataStructs.forEach { ds ->
            ds.Functions.forEach { function ->
                function.FunctionCalls.forEach { call ->
                    if (call.NodeName.startsWith("Service") && call.NodeName.contains(".") && !call.NodeName.contains(".client")) {
                        val split = call.NodeName.split(".")
                        val struct = split.first()
                        val model = split.drop(1).joinToString(".")

                        val serviceStruct = currentDsMap[struct] ?: return@forEach
                        val serviceField =
                            serviceStruct.map { it.Fields.filter { field -> field.TypeValue == model } }.flatten()

                        serviceField.forEach { codeField ->
                            val typeType = codeField.TypeType.removePrefix("*")
                            val importPath = typeType.split(".").first()

                            importPath.let {
                                val import = importMap[it]
                                val targetFile = allDs[import?.Source ?: return@let]
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
                        if (imports.map { it.Source }.contains("net/rpc")) {
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
        ds.FilePath
            .replace(parentSpace, "")
            .removePrefix("/")
            .split(".").dropLast(1).joinToString("/") + "$" + ds.NodeName + "." + function.Name
}