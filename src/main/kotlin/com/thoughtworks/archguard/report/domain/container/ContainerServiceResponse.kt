package com.thoughtworks.archguard.report.domain.container

class ContainerServiceResponse(
    val id: String = "",
    val name: String = "",
    val demands: List<ContainerDemand>,
    val resources: List<ContainerResource>,
)

class ContainerDemand(
    val systemId: String,
    val sourcePackage: String,
    val sourceClass: String,
    val sourceMethod: String,
    val targetUrl: String,
    val targetHttpMethod: String,
)

class ContainerResource(
    val systemId: String,
    val sourceUrl: String,
    val sourceHttpMethod: String,
    val packageName: String,
    val className: String,
    val methodName: String
)
