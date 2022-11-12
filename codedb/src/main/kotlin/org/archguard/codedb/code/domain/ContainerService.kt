package org.archguard.codedb.code.domain

import kotlinx.serialization.Serializable
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Serializable
@Document
data class ContainerService(
    @Id val id: String,
    val systemId: String,
    val language: String,
    val path: String,
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
    var methodName: String = ""
)
