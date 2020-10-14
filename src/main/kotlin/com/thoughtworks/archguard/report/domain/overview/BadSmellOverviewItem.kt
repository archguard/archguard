package com.thoughtworks.archguard.report.domain.overview

import com.thoughtworks.archguard.report.domain.badsmell.DashboardGroup
import com.thoughtworks.archguard.report.domain.badsmell.BadSmellLevel
import com.thoughtworks.archguard.report.domain.badsmell.BadSmellType

class BadSmellOverviewItem(eBadSmell: BadSmellType, var level: BadSmellLevel, var count: Long) {
    var badSmell: String = eBadSmell.value
    var category: String = DashboardGroup.getGroup(eBadSmell).value
}
