package com.thoughtworks.archguard.datamap.controller.domain

data class Datamap(
    val id: Long,
    val packageName: String,
    val className: String,
    val functionName: String,
    val systemId: String,
    val tables: String
)
