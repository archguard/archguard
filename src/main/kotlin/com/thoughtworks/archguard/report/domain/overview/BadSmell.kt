package com.thoughtworks.archguard.report.domain.overview

enum class BadSmell(val value: String) {
    METHOD_OVER_SIZING("体量过大-方法过大"),
    CLASS_OVER_SIZING("体量过大-类过大"),
    PACKAGE_OVER_SIZING("体量过大-包过过大"),
    MODULE_OVER_SIZING("体量过大-子模块过大"),
    COUPLING_CLASS_HUB("耦合度-枢纽模块"),
    COUPLING_DATA_CLUMPS("耦合度-数据泥团"),
    COUPLING_DEEP_INHERITANCE("耦合度-过深继承"),
    COUPLING_CYCLE_DEPENDENCY("耦合度-循环依赖")
}
