package com.thoughtworks.archguard.report.domain.overview

import com.thoughtworks.archguard.report.controller.BadSmellLevel
import com.thoughtworks.archguard.report.controller.BadSmellType
import com.thoughtworks.archguard.report.controller.DashboardGroup

class BadSmellOverviewItem(var badSmell: BadSmellType, var category: DashboardGroup, var level: BadSmellLevel, var count: Long)
