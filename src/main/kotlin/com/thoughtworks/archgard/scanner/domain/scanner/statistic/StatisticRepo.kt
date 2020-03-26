package com.thoughtworks.archgard.scanner.domain.scanner.statistic

interface StatisticRepo {
    fun save(statistic: List<Statistic>)
    fun delete()
}
