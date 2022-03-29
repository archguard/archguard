package org.archguard.scanner.sourcecode.frontend

import org.archguard.scanner.sourcecode.frontend.identify.AxiosHttpIdentify
import org.archguard.scanner.sourcecode.frontend.identify.UmiHttpIdentify
import chapi.app.frontend.path.ecmaImportConvert
import chapi.app.frontend.path.relativeRoot
import chapi.domain.core.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.scanner.common.container.ContainerDemand
import org.archguard.scanner.common.container.ContainerService
import java.io.File

@Serializable
data class ApiCodeCall(val ApiType: String = "") : CodeCall() {
    companion object {
        fun from(call: CodeCall, apiType: String): ApiCodeCall {
            val apiCodeCall = ApiCodeCall(ApiType = apiType)
            apiCodeCall.NodeName = call.NodeName
            apiCodeCall.FunctionName = call.FunctionName
            apiCodeCall.Parameters = call.Parameters
            apiCodeCall.Position = call.Position
            apiCodeCall.Type = call.Type
            apiCodeCall.Package = call.Package

            return apiCodeCall
        }
    }
}

class FrontendApiAnalyser {
    private var componentCallMap: HashMap<String, MutableList<String>> = hashMapOf()
    private var componentInbounds: HashMap<String, MutableList<String>> = hashMapOf()
    private var callMap: HashMap<String, CodeCall> = hashMapOf()
    private var httpAdapterMap: HashMap<String, ApiCodeCall> = hashMapOf()

    // for Axios Http Call
    private val axiosIdent = AxiosHttpIdentify()
    private val umiIdent = UmiHttpIdentify()

    // 1. first create Component with FunctionCall maps based on Import
    // 2. build axios/umi-request to an API call method
    // 3. mapping for results
    fun analysisByPath(nodes: Array<CodeDataStruct>, workspace: String): Array<ContainerService> {
        nodes.forEach { node ->
            analysisByNode(node, workspace)
        }

        return toContainerServices()
    }

    fun analysisByNode(node: CodeDataStruct, workspace: String) {
        var isComponent: Boolean
        val isComponentExt = node.fileExt() == "tsx" || node.fileExt() == "jsx"
        val isNotInterface = node.Type != DataStructType.INTERFACE

        val inbounds = createInbounds(workspace, node.Imports, node.FilePath)

        val moduleName = relativeRoot(workspace, node.FilePath).substringBeforeLast('.', "")
        val componentName = naming(moduleName, node.NodeName)

        node.Fields.forEach { field ->
            fieldToCallMap(field, componentName, inbounds)
        }

        // lookup CodeCall from Functions
        node.Functions.forEach { func ->
            isComponent = isNotInterface && isComponentExt && func.IsReturnHtml
            if (isComponent) {
                componentCallMap[componentName] = mutableListOf()
            }

            var calleeName = naming(componentName, func.Name)
            if (isComponent) {
                calleeName = componentName
            }

            func.FunctionCalls.forEach { call ->
                callMap[calleeName] = call
                // TODO: refactor by DI
                if (axiosIdent.isMatch(call, node.Imports)) {
                    httpAdapterMap[calleeName] = ApiCodeCall.from(call, "axios")
                }
                if (umiIdent.isMatch(call, node.Imports)) {
                    httpAdapterMap[calleeName] = ApiCodeCall.from(call, "umi")
                }
                if (isComponent) {
                    componentCallMap[componentName]?.plusAssign((call.FunctionName))
                }
            }

            recursiveCall(func, calleeName, isComponent, componentName, node.Imports)

            if (isComponent) {
                componentInbounds[componentName] = inbounds
            }
        }
    }

    fun toContainerServices(): Array<ContainerService> {
        File("component.inbounds.json").writeText(Json.encodeToString(componentInbounds))
        var componentCalls: Array<ContainerService> = arrayOf()
        componentInbounds.forEach { map ->
            val componentRef = ContainerService(name = map.key)
            map.value.forEach {
                // TODO: add support for multiple level call routes
                if (httpAdapterMap[it] != null) {
                    val call = httpAdapterMap[it]!!

                    var httpApi = ContainerDemand()
                    when (call.ApiType) {
                        "axios" -> {
                            httpApi = axiosIdent.convert(call)
                        }
                        "umi" -> {
                            httpApi = umiIdent.convert(call)
                        }
                    }

                    httpApi.source_caller = it
                    httpApi.call_routes = listOf(it)
                    componentRef.demands += httpApi
                } else {
                    if (callMap[it] != null) {
                        val codeCall = callMap[it]!!
                        val name = naming(codeCall.NodeName, codeCall.FunctionName)

                        if (httpAdapterMap[name] != null) {
                            val call = httpAdapterMap[name]!!

                            var httpApi = ContainerDemand()
                            when (call.ApiType) {
                                "axios" -> {
                                    httpApi = axiosIdent.convert(call)
                                }
                                "umi" -> {
                                    httpApi = umiIdent.convert(call)
                                }
                            }

                            httpApi.source_caller = name
                            httpApi.call_routes = listOf(it, name)
                            componentRef.demands += httpApi
                        }
                    }
                }
            }

            if (componentRef.demands.isNotEmpty()) {
                componentCalls += componentRef
            }
        }
        return componentCalls
    }

    private fun fieldToCallMap(
        field: CodeField,
        componentName: String,
        inbounds: MutableList<String>
    ) {
        field.Calls.forEach {
            val calleeName = naming(componentName, field.TypeKey)
            callMap[calleeName] = it

            it.Parameters.forEach {
                inbounds.forEach { inbound ->
                    if (inbound.endsWith("::${it.TypeValue}")) {
                        val split = inbound.split("::")
                        callMap[calleeName] = CodeCall(FunctionName = split[1], NodeName = split[0])
                    }
                }
            }
        }
    }

    private fun createInbounds(workspace: String, imports: Array<CodeImport>, filePath: String): MutableList<String> {
        val inbounds: MutableList<String> = mutableListOf()

        imports.forEach { imp ->
            imp.UsageName.forEach {
                val source = ecmaImportConvert(workspace, filePath, imp.Source)
                inbounds += naming(source, it)
            }
        }

        return inbounds
    }

    private fun recursiveCall(
        func: CodeFunction,
        calleeName: String,
        isComponent: Boolean,
        componentName: String,
        imports: Array<CodeImport>
    ) {
        func.InnerFunctions.forEach { inner ->
            run {
                inner.FunctionCalls.forEach { innerCall ->
                    run {
                        callMap[calleeName] = innerCall

                        if (umiIdent.isMatch(innerCall, imports)) {
                            httpAdapterMap[calleeName] = ApiCodeCall.from(innerCall, "umi")
                        }

                        if (axiosIdent.isMatch(innerCall, imports)) {
                            httpAdapterMap[calleeName] = ApiCodeCall.from(innerCall, "axios")
                        }

                        if (isComponent) {
                            componentCallMap[componentName]?.plusAssign((innerCall.FunctionName))
                        }
                    }
                }
            }

            func.InnerFunctions.forEach {
                recursiveCall(it, calleeName, isComponent, componentName, imports)
            }
        }
    }
}
