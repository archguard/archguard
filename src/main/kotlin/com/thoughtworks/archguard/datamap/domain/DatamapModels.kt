package com.thoughtworks.archguard.datamap.domain

data class Datamap(
    val id: String,
    val packageName: String,
    val className: String,
    val functionName: String,
    val systemId: String,
    val tables: String
)
