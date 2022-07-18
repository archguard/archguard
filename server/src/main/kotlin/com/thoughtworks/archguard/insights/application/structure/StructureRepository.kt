package com.thoughtworks.archguard.insights.application.structure

import com.thoughtworks.archguard.insights.application.StructureModelDto
import org.archguard.domain.insight.Query

interface StructureRepository {
    fun filterByCondition(query: Query) : List<StructureModelDto>
    fun filterByConditionWithSystemId(id: Long, query: Query): List<StructureModelDto>
}