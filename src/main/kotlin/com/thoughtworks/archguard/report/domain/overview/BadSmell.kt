package com.thoughtworks.archguard.report.domain.overview

enum class BadSmell(val value: String) {
    METHOD_OVER_SIZING("体量过大-方法过大"),
    CLASS_OVER_SIZING("体量过大-类过大"),
    PACKAGE_OVER_SIZING("体量过大-包过过大"),
    MODULE_OVER_SIZING("体量过大-子模块过大")
}
