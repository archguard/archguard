package com.thoughtworks.archguard.smartscanner.dto


data class ContainerService(
    // component name, only if is a component
    var name: String = "",
    var demands: List<ContainerDemand> = listOf(),
    var resources: List<ContainerResource> = listOf()
)

data class ContainerDemand(
    var source_caller: String = "",
    var call_routes: List<String> = listOf(),
    var base: String = "",
    var target_url: String = "",
    var target_http_method: String = "",
    var call_data: String = ""
)

data class ContainerResource(
    var sourceUrl: String = "",
    var sourceHttpMethod: String = "",
    var packageName: String = "",
    var className: String = "",
    var methodName: String = ""
)
