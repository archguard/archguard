package com.thoughtworks.archguard.metrics.appl

import com.thoughtworks.archguard.metrics.domain.ClassMetricRepository
import com.thoughtworks.archguard.metrics.domain.abc.AbcService
import com.thoughtworks.archguard.metrics.domain.dit.DitService
import com.thoughtworks.archguard.metrics.domain.lcom4.LCOM4Service
import com.thoughtworks.archguard.metrics.domain.noc.NocService
import com.thoughtworks.archguard.metrics.infrastructure.ClassMetricsDtoListForWriteInfluxDB
import com.thoughtworks.archguard.metrics.infrastructure.InfluxDBClient
import com.thoughtworks.archguard.module.domain.model.JClassVO
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class MetricPersistServiceImpl(val abcService: AbcService,
                               val ditService: DitService,
                               val lcoM4Service: LCOM4Service,
                               val nocService: NocService,
                               val classMetricRepository: ClassMetricRepository,
                               val influxDBClient: InfluxDBClient) : MetricPersistService {
    private val log = LoggerFactory.getLogger(MetricPersistServiceImpl::class.java)

    override fun persistClassMetrics(systemId: Long) {
        val allAbc = abcService.calculateAllAbc(systemId)
        val abcMap: MutableMap<String, Int> = mutableMapOf()
        allAbc.forEach { abcMap[it.jClassVO.id!!] = it.abcValue }
        log.info("Finish calculate all abc, count: {}", abcMap.keys.size)

        val allDit = ditService.calculateAllDit(systemId)
        val ditMap: MutableMap<String, Int> = mutableMapOf()
        allDit.forEach { ditMap[it.jClassVO.id!!] = it.ditValue }
        log.info("Finish calculate all dit, count: {}", ditMap.keys.size)

        val allNoc = nocService.calculateAllNoc(systemId)
        val nocMap: MutableMap<String, Int> = mutableMapOf()
        allNoc.forEach { nocMap[it.jClassVO.id!!] = it.nocValue }
        log.info("Finish calculate all noc, count: {}", nocMap.keys.size)

        val allLCOM4 = lcoM4Service.calculateAllLCOM4(systemId)
        val lcom4Map: MutableMap<String, Int> = mutableMapOf()
        allLCOM4.forEach { lcom4Map[it.jClassVO.id!!] = it.lcom4Value }
        log.info("Finish calculate all lcom4, count: {}", lcom4Map.keys.size)

        val allJClassVO = allAbc.map { it.jClassVO }
        val classMetricPOs = mutableListOf<ClassMetricPO>()
        val classMetrics = mutableListOf<ClassMetric>()

        allJClassVO.forEach { classMetricPOs.add(ClassMetricPO(systemId, it.id!!, abcMap[it.id!!], ditMap[it.id!!], nocMap[it.id!!], lcom4Map[it.id!!])) }
        allJClassVO.forEach { classMetrics.add(ClassMetric(systemId, it, abcMap[it.id!!], ditMap[it.id!!], nocMap[it.id!!], lcom4Map[it.id!!])) }

        classMetricRepository.insertOrUpdateClassMetricPOs(systemId, classMetricPOs)
        influxDBClient.save(ClassMetricsDtoListForWriteInfluxDB(classMetrics).toRequestBody())
    }
}

data class ClassMetricPO(val systemId: Long, val classId: String, val abc: Int?, val dit: Int?, val noc: Int?, val lcom4: Int?)
data class ClassMetric(val systemId: Long, val jClassVO: JClassVO, val abc: Int?, val dit: Int?, val noc: Int?, val lcom4: Int?)
