package com.thoughtworks.archguard.metrics.domain

import org.springframework.stereotype.Service

@Service
class MetricPersistServiceImpl(val metricsService: MetricsService, val classMetricRepository: ClassMetricRepository) : MetricPersistService {
    override fun persistClassMetrics(projectId: Int) {
        val allAbc = metricsService.calculateAllAbc()
        val abcMap: MutableMap<String, Int> = mutableMapOf()
        allAbc.forEach { abcMap[it.jClassVO.id!!] = it.abcValue }

        val allDit = metricsService.calculateAllDit()
        val ditMap: MutableMap<String, Int> = mutableMapOf()
        allDit.forEach { ditMap[it.jClassVO.id!!] = it.ditValue }

        val allNoc = metricsService.calculateAllNoc()
        val nocMap: MutableMap<String, Int> = mutableMapOf()
        allNoc.forEach { nocMap[it.jClassVO.id!!] = it.nocValue }

        val allLCOM4 = metricsService.calculateAllLCOM4()
        val lcom4Map: MutableMap<String, Int> = mutableMapOf()
        allLCOM4.forEach { lcom4Map[it.jClassVO.id!!] = it.lcom4Value }

        val allJClassVO = allAbc.map { it.jClassVO }
        val classMetricPOs = mutableListOf<ClassMetricPO>()
        allJClassVO.forEach { classMetricPOs.add(ClassMetricPO(projectId, it.id!!, abcMap[it.id!!], ditMap[it.id!!], nocMap[it.id!!], lcom4Map[it.id!!])) }
        classMetricRepository.insertClassMetricPOs(classMetricPOs)
    }
}

data class ClassMetricPO(val projectId: Int, val clazzId: String, val abc: Int?, val dit: Int?, val noc: Int?, val lcom4: Int?)