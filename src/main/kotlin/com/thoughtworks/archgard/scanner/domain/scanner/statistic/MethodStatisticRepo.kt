package com.thoughtworks.archgard.scanner.domain.scanner.statistic

interface MethodStatisticRepo {
    fun save(methodStatistic: List<MethodStatistic>)
    fun delete()
}
