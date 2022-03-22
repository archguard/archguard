package com.thoughtworks.archguard.scanner.sourcecode.frontend

import kotlinx.serialization.Serializable

@Serializable
data class ContainerService(
    // component name, only if is a component
    var name: String = "",
    var demands: List<ContainerDemand> = listOf(),
    var resources: List<ContainerDemand> = listOf()
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
