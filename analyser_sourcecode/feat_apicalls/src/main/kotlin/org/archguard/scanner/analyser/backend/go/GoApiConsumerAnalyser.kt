package org.archguard.scanner.analyser.backend.go

import chapi.domain.core.CodeContainer
import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import chapi.domain.core.CodeImport

class GoApiConsumerAnalyser(dataStructs: List<CodeDataStruct>) {
    private val allDs: Map<String, List<CodeDataStruct>> = dataStructs.groupBy {
        it.FilePath.split(".").dropLast(1).joinToString("/")
    }

    fun analysis(input: CodeContainer): MutableMap<String, String> {
        val currentDsMap = input.DataStructures.groupBy {
            it.NodeName
        }

        val sourceTargetMap: MutableMap<String, String> = mutableMapOf()
        input.DataStructures.forEach { ds ->
            ds.Functions.forEach { function ->
                function.FunctionCalls.forEach { call ->
                    if (call.NodeName.startsWith("Service") && call.NodeName.contains(".")) {
                        val split = call.NodeName.split(".")
                        val struct = split.first()
                        val model = split.drop(1).joinToString(".")

                        val codeDataStruct = currentDsMap[struct] ?: return@forEach
                        val field =
                            codeDataStruct.map { it.Fields.filter { field -> field.TypeValue == model } }.flatten()

                        val importMap: MutableMap<String, CodeImport> = mutableMapOf()
                        input.Imports.forEach { codeImport ->
                            codeImport.UsageName.forEach { it ->
                                importMap[it] = codeImport
                            }
                        }

                        field.forEach { codeField ->
                            val typeType = codeField.TypeType.removePrefix("*")
                            val importPath = typeType.split(".").first()

                            importPath.let {
                                val import = importMap[it]
                                val targetFile = allDs[import!!.Source]
                                val s = pathify(ds, function)
                                //
                                val callName = call.FunctionName
                                targetFile?.forEach { ds ->
                                    ds.Functions.forEach { targetFunction ->
                                        if (targetFunction.Name == callName) {
                                            sourceTargetMap[s] = pathify(ds, targetFunction)
                                        }
                                    }
                                }
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