package org.archguard.smell

class BadSmellOverviewItem(eBadSmell: BadSmellType, var level: BadSmellLevel, var count: Long) {
    var badSmell: String = eBadSmell.value
    var category: String = DashboardGroup.getGroup(eBadSmell).value
}
