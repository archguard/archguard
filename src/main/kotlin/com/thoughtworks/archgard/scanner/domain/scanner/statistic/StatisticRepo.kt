package com.thoughtworks.archgard.scanner.domain.scanner.statistic

interface StatisticRepo {
    fun save(classStatistic: List<ClassStatistic>)
    fun delete()
}
