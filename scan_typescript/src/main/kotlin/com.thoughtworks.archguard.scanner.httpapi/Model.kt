package com.thoughtworks.archguard.scanner.httpapi


@Sql("service_demand")
data class Demand(
    @Sql("id") val id: String,
    @Sql("targetUrl") val targetUrl: String,
    @Sql("httpMethod") val httpMethod: String,
    @Sql("caller") val caller: String,
)

@Sql("service_callchain")
data class ServiceApi(
    @Sql("system_id") val systemId: Long,
    @Sql("demandIds") val demandIds: String,
    @Sql("resourceIds") val resourceIds: String
)

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class Sql(val value: String)
