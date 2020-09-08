package com.thoughtworks.archgard.scanner2.appl

import com.thoughtworks.archgard.scanner2.domain.*
import com.thoughtworks.archgard.scanner2.domain.model.ClassMetric
import com.thoughtworks.archgard.scanner2.infrastructure.influx.ClassMetricsDtoListForWriteInfluxDB
import com.thoughtworks.archgard.scanner2.infrastructure.influx.InfluxDBClient
import com.thoughtworks.archgard.scanner2.infrastructure.persist.ClassMetricPO
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class MetricPersistApplService(val abcService: AbcService,
                               val ditService: DitService,
                               val lcoM4Service: LCOM4Service,
                               val nocService: NocService,
                               val metricRepository: MetricRepository,
                               val influxDBClient: InfluxDBClient) {
    private val log = LoggerFactory.getLogger(MetricPersistApplService::class.java)

    fun persistLevel2Metrics(systemId: Long) {
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

        metricRepository.insertOrUpdateClassMetricPOs(systemId, classMetricPOs)
        influxDBClient.save(ClassMetricsDtoListForWriteInfluxDB(classMetrics).toRequestBody())
    }
}

