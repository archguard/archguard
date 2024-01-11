package org.archguard.scanner.analyser.frontend

import chapi.domain.core.*
import kotlinx.serialization.Serializable
import org.archguard.scanner.analyser.base.ApiAnalyser
import org.archguard.scanner.analyser.frontend.identify.AxiosHttpIdentify
import org.archguard.scanner.analyser.frontend.identify.UmiHttpIdentify
import org.archguard.scanner.analyser.frontend.path.ecmaImportConvert
import org.archguard.scanner.analyser.frontend.path.relativeRoot
import org.archguard.scanner.core.sourcecode.ContainerDemand
import org.archguard.scanner.core.sourcecode.ContainerService
import org.archguard.scanner.core.sourcecode.ContainerSupply

@Serializable
class ApiCodeCall(
    val ApiType: String = "",
    var Package: String = "",
    var Type: CallType = CallType.FUNCTION,
    var NodeName: String = "",
    var FunctionName: String = "",
    var Parameters: List<CodeProperty> = listOf(),
    var Position: CodePosition = CodePosition(),
    var OriginNodeName: String = "",
) {
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

class FrontendApiAnalyser(override var resources: List<ContainerSupply> = listOf()) : ApiAnalyser {
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
    fun analysisByPath(nodes: Array<CodeDataStruct>, workspace: String): List<ContainerService> {
        nodes.forEach { node ->
            analysisByNode(node, workspace)
        }

        return toContainerServices()
    }

    override fun analysisByNode(node: CodeDataStruct, workspace: String) {
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

    class LoopDepth(var index: Int)

    override fun toContainerServices(): List<ContainerService> {
        val componentCalls: MutableList<ContainerService> = mutableListOf()

        componentInbounds.forEach { inbound ->
            val componentRef = ContainerService(name = inbound.key)
            inbound.value.forEach {
                // TODO: add support for multiple level call routes
                if (httpAdapterMap[it] != null) {
                    val routes = listOf(it)
                    componentRef.demands += createHttpApi(it, routes)
                } else {
                    lookupSource(it, componentRef, LoopDepth(1))
                }
            }

            if (componentRef.demands.isNotEmpty()) {
                componentCalls += componentRef
            }
        }
        return componentCalls
    }

    private val DEFAULT_LOOP_API_CALL_DEPTH = 3
    private fun lookupSource(methodName: String, componentRef: ContainerService, loopDepth: LoopDepth) {
        if (loopDepth.index > DEFAULT_LOOP_API_CALL_DEPTH) {
            return
        }
        loopDepth.index++
        if (callMap[methodName] != null) {
            val codeCall = callMap[methodName]!!
            val name = naming(codeCall.NodeName, codeCall.FunctionName)

            if (httpAdapterMap[name] != null) {
                val routes = listOf(methodName, name)
                componentRef.demands += createHttpApi(name, routes)
            } else {
                lookupSource(name, componentRef, loopDepth)
            }
        }
    }

    private fun createHttpApi(
        name: String,
        routes: List<String>
    ): ContainerDemand {
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
        httpApi.call_routes = routes
        return httpApi
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

    private fun createInbounds(workspace: String, imports: List<CodeImport>, filePath: String): MutableList<String> {
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
        imports: List<CodeImport>
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
