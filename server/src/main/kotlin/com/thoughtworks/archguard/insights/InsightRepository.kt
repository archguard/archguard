package com.thoughtworks.archguard.insights

interface InsightRepository {
    abstract fun filterByCondition(id: Long): List<ScaModelDto>
}
