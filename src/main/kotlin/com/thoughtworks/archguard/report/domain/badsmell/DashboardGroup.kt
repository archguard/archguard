package com.thoughtworks.archguard.report.domain.badsmell

enum class DashboardGroup(val value: String, val badSmells: List<BadSmellType>) {
    COUPLING("过高耦合", listOf(
            BadSmellType.DATACLUMPS,
            BadSmellType.DEEPINHERITANCE,
            BadSmellType.CLASSHUB,
            BadSmellType.METHODHUB,
            BadSmellType.PACKAGEHUB,
            BadSmellType.MODULEHUB,
            BadSmellType.CYCLEDEPENDENCY)),
    SIZING("体量过大", listOf(
            BadSmellType.SIZINGMODULES,
            BadSmellType.SIZINGPACKAGE,
            BadSmellType.SIZINGMETHOD,
            BadSmellType.SIZINGCLASS)),
    COHESION("内聚度不足", listOf(
            BadSmellType.DATA_CLASS,
            BadSmellType.SHOTGUN_SURGERY)),
    REDUNDANCY("冗余度高", listOf(
            BadSmellType.REDUNDANT_ELEMENT,
            BadSmellType.OVER_GENERALIZATION)),
    UNDEFINED("未找到", listOf());

    companion object {
        fun getGroup(badSmellType: BadSmellType): DashboardGroup {
            return values().find { it.badSmells.contains(badSmellType) } ?: UNDEFINED
        }
    }
}
