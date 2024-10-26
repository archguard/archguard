package org.archguard.context

import kotlinx.serialization.Serializable

@Serializable
data class ContainerService(
    // component name, only if is a component
    var name: String = "",
    var demands: List<ContainerDemand> = listOf(),
    var resources: List<ContainerSupply> = listOf()
)

@Serializable
data class ContainerDemand(
    var source_caller: String = "",
    var call_routes: List<String> = listOf(),
    var base: String = "",
    var target_url: String = "",
    var target_http_method: String = "",
    var call_data: String = ""
)

@Serializable
data class ContainerSupply(
    var sourceUrl: String = "",
    var sourceHttpMethod: String = "",
    var packageName: String = "",
    var className: String = "",
    var methodName: String = "",
    var supplyType: ServiceSupplyType = ServiceSupplyType.HTTP_API
)


enum class ServiceSupplyType {
    HTTP_API,
    DUBBO_API,
    PROTO_RPC_API
}

