package com.thoughtworks.archgard.scanner.domain.scanner.statistic

data class Statistic(
        val id: String,
        val projectName: String,
        val packageName: String,
        val typeName: String,
        val lines: Int)
