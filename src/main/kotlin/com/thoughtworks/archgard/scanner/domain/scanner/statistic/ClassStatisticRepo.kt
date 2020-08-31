package com.thoughtworks.archgard.scanner.domain.scanner.statistic

interface ClassStatisticRepo {
    fun save(classStatistic: List<ClassStatistic>)
    fun delete()
}
