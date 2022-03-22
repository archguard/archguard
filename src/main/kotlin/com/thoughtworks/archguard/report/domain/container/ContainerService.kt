package com.thoughtworks.archguard.report.domain.container

class ContainerService(
    val name: String,
    val containerType: String,
    val scmAddress: String,
)

class ContainerDemand(
    val sourcePackage: String,
    val sourceClass: String,
    val sourceMethod: String,
    val targetUrl: String,
    val targetHttpMethod: String,
)

class ContainerResource(
    val sourceUrl: String,
    val sourceHttpMethod: String,
    val packageName: String,
    val className: String,
    val methodName: String
)
