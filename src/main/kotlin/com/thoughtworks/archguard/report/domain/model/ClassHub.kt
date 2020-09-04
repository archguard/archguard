package com.thoughtworks.archguard.report.domain.model

data class ClassHub(val systemId: Long,
                    val moduleName: String? = null,
                    val packageName: String,
                    val typeName: String,
                    val fanIn: Int,
                    val fanOut: Int)