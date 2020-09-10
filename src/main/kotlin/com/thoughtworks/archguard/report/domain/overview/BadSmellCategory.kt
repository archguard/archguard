package com.thoughtworks.archguard.report.domain.overview

enum class BadSmellCategory(val value: String) {
    OVER_SIZING("体量过大"),
    COUPLING("过高耦合"),
    DESIGN_REDUNDANCY("设计冗余"),
    LOW_COHESION("过低内聚"),
    COMPLEX("过低内聚"),
    LACK_STRATIFICATION("缺乏分层")
}
