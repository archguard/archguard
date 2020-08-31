package com.thoughtworks.archgard.scanner.domain.scanner.statistic

data class Statistic(
        val id: String,
        val systemName: String,
        val packageName: String,
        val typeName: String,
        val lines: Int,
        val fanIn: Int,
        val fanOut: Int)
