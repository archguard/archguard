package com.thoughtworks.archguard.report.domain.badsmell

enum class ThresholdKey(val dimension: String, val key: String, val condition: String) {
    SIZING_MODULE_BY_LOC("体量维度", "过大的模块", "模块的总代码行数 > 指标阈值"),
    SIZING_MODULE_BY_PACKAGE_COUNT("体量维度", "过大的模块", "模块中包含包的个数 > 指标阈值"),
    SIZING_PACKAGE_BY_LOC("体量维度", "过大的包", "包的总代码行数（不含import行数） > 指标阈值"),
    SIZING_PACKAGE_BY_CLASS_COUNT("体量维度", "过大的包", "包中包含的类个数 > 指标阈值"),
    SIZING_CLASS_BY_LOC("体量维度", "过大的类", "类的代码行数 > 指标阈值"),
    SIZING_CLASS_BY_FUNC_COUNT("体量维度", "过大的类", "类中包含的方法个数 > 指标阈值"),
    SIZING_METHOD_BY_LOC("体量维度", "过大的方法", "方法代码行数（含空行） > 指标阈值"),

    COUPLING_HUB_MODULE("耦合纬度", "枢纽模块", "出向依赖或入向依赖 > 指标阈值"),
    COUPLING_HUB_PACKAGE("耦合纬度", "枢纽包", "出向依赖或入向依赖 > 指标阈值"),
    COUPLING_HUB_CLASS("耦合纬度", "枢纽类", "出向依赖或入向依赖 > 指标阈值"),
    COUPLING_HUB_METHOD("耦合纬度", "枢纽方法", "出向依赖或入向依赖 > 指标阈值"),
    COUPLING_DATA_CLUMPS("耦合纬度", "数据泥团", "缺乏内聚指标（LCOM） > 指标阈值"),
    COUPLING_DEEP_INHERITANCE("耦合纬度", "过深继承", "继承深度 > 指标阈值"),
    COUPLING_CIRCULAR("耦合纬度", "循环依赖", "循环依赖的数量 > 指标阈值")
}
