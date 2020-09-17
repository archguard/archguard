package com.thoughtworks.archguard.report.domain.overview

import com.thoughtworks.archguard.report.controller.BadSmellLevel
import com.thoughtworks.archguard.report.controller.BadSmellType
import com.thoughtworks.archguard.report.controller.DashboardGroup

class BadSmellOverviewItem(eBadSmell: BadSmellType, eCategory: DashboardGroup, var level: BadSmellLevel, var count: Long) {
    var badSmell: String = eBadSmell.value
    var category: String = eCategory.value
}
