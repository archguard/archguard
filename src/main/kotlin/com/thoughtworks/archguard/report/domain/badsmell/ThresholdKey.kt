package com.thoughtworks.archguard.report.domain.badsmell

enum class ThresholdKey(val dimension: String, val key: String, val condition: String) {
    SIZING_MODULE_BY_LOC("体量维度", "过大的模块", "模块代码行数 > 指标阈值"),
    SIZING_PACKAGE_BY_LOC("体量维度", "过大的包", "包代码行数 > 指标阈值"),
    SIZING_PACKAGE_BY_CLASS_COUNT("体量维度", "过大的类", "方法个数 > 指标阈值"),
    SIZING_CLASS_BY_LOC("体量维度", "过大的类", "类代码行数 > 指标阈值"),
    SIZING_CLASS_BY_FUNC_COUNT("体量维度", "过大的类", "方法个数 > 指标阈值"),
    SIZING_METHOD_BY_LOC("体量维度", "过大的方法", "方法代码行数 > 指标阈值"),

    COUPLING_HUB_MODULE("耦合纬度", "枢纽模块", "出向依赖或入向依赖 > 指标阈值"),
    COUPLING_HUB_PACKAGE("耦合纬度", "枢纽包", "出向依赖或入向依赖 > 指标阈值"),
    COUPLING_HUB_CLASS("耦合纬度", "枢纽类", "出向依赖或入向依赖 > 指标阈值"),
    COUPLING_HUB_METHOD("耦合纬度", "枢纽方法", "出向依赖或入向依赖 > 指标阈值"),
}