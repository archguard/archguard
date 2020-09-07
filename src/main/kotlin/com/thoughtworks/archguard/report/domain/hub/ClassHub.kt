package com.thoughtworks.archguard.report.domain.hub

data class ClassHub(val id: String,
                    val systemId: Long,
                    val moduleName: String? = null,
                    val packageName: String,
                    val typeName: String,
                    val fanIn: Int,
                    val fanOut: Int)