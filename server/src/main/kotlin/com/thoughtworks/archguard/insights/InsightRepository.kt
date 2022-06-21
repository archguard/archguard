package com.thoughtworks.archguard.insights

interface InsightRepository {
    abstract fun filterByCondition(id: Long, mappings: MutableMap<String, String>): List<ScaModelDto>
}
