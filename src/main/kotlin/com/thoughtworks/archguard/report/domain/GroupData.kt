package com.thoughtworks.archguard.report.domain

import com.thoughtworks.archguard.report.application.GraphData
import com.thoughtworks.archguard.report.domain.badsmell.BadSmellLevel
import com.thoughtworks.archguard.report.domain.badsmell.BadSmellType

class GroupData(eBadSmellType: BadSmellType, val level: BadSmellLevel, val graphData: List<GraphData>) {
    var type: String = eBadSmellType.value
}