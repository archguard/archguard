package com.thoughtworks.archgard.scanner.domain.scanner.statistic

data class ClassStatistic(
        val id: String,
        val projectName: String,
        val packageName: String,
        val typeName: String,
        val lines: Int,
        val fanIn: Int,
        val fanOut: Int)
