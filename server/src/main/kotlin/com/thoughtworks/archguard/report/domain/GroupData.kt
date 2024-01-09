package com.thoughtworks.archguard.report.domain

import com.thoughtworks.archguard.report.application.GraphData
import org.archguard.smell.BadSmellLevel
import org.archguard.smell.BadSmellType

class GroupData(eBadSmellType: BadSmellType, val level: BadSmellLevel, val graphData: List<GraphData>) {
    var type: String = eBadSmellType.value
}
