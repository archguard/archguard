package org.archguard.scanner.analyser.backend.go

import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import chapi.domain.core.CodeImport
import org.archguard.context.ContainerDemand

class GoProtobufConsumerAnalyser(dataStructs: List<CodeDataStruct>) {
    private val allDs: Map<String, List<CodeDataStruct>> = dataStructs.groupBy {
        it.FilePath.split(".").dropLast(1).joinToString("/")
    }

    fun analysis(): List<ContainerDemand> {
        // var source_caller: String = "",
        //    var call_routes: List<String> = listOf(),
        //    var base: String = "",
        //    var target_url: String = "",
        //    var target_http_method: String = "",
        //    var call_data: String = ""
        return listOf()
    }

    fun analyzeAndMapCodePaths(codeDataStructs: List<CodeDataStruct>): MutableMap<String, String> {
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

        val sourceTargetMap: MutableMap<String, String> = mutableMapOf()
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
                                            sourceTargetMap[source] = pathify(ds, targetFunction)
                                        }
                                    }
                                }
                            }
                        }
                    } else if (call.NodeName == "Service.client" && call.FunctionName == "Call") {
                        if (imports.map { it.Source }.contains("net/rpc")) {
                            /// get second Call parameters
                            if (call.Parameters.size > 1) {
                                val secondCall = call.Parameters[1]
                                sourceTargetMap[pathify(ds, function)] = secondCall.TypeValue.removeSurrounding("\"")
                            }
                        }
                    }
                }
            }
        }

        return sourceTargetMap
    }

    private fun pathify(ds: CodeDataStruct, function: CodeFunction) =
        ds.FilePath.split(".").dropLast(1).joinToString("/") + "$" + ds.NodeName + "." + function.Name
}