package com.thoughtworks.archgard.scanner.domain.scanner.statistic

data class MethodStatistic(
        val id: String,
        val systemId: Long,
        val moduleName: String? = null,
        val packageName: String,
        val typeName: String,
        val methodName: String,
        val lines: Int)

