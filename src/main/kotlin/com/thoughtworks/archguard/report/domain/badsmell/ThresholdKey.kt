package com.thoughtworks.archguard.report.domain.badsmell

enum class ThresholdKey(val dimension: String, val key: String, val condition: String) {
    SIZING_CLASS_BY_LOC("体量维度", "过大的类", "方法代码行数 > 指标阈值"),
    SIZING_CLASS_BY_FUNC_COUNT("体量维度", "过大的类", "方法个数 > 指标阈值")
}