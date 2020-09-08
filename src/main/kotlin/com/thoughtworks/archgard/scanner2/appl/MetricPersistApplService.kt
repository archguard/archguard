package com.thoughtworks.archgard.scanner2.appl

import com.thoughtworks.archgard.scanner2.domain.*
import com.thoughtworks.archgard.scanner2.domain.model.ClassMetric
import com.thoughtworks.archgard.scanner2.infrastructure.influx.ClassMetricsDtoListForWriteInfluxDB
import com.thoughtworks.archgard.scanner2.infrastructure.influx.InfluxDBClient
import org.springframework.stereotype.Service

@Service
class MetricPersistApplService(val abcService: AbcService,
                               val ditService: DitService,
                               val lcoM4Service: LCOM4Service,
                               val nocService: NocService,
                               val jClassRepository: JClassRepository,
                               val metricRepository: MetricRepository,
                               val influxDBClient: InfluxDBClient) {

    fun persistLevel2Metrics(systemId: Long) {

        val jClasses = jClassRepository.getJClassesHasModules(systemId)

        val abcMap = abcService.calculateAllAbc(systemId, jClasses)
        val ditMap = ditService.calculateAllDit(systemId, jClasses)
        val nocMap = nocService.calculateAllNoc(systemId, jClasses)
        val lcom4Map = lcoM4Service.calculateAllLCOM4(systemId, jClasses)

        val classMetrics = mutableListOf<ClassMetric>()
        jClasses.forEach {
            classMetrics.add(ClassMetric(systemId, it.toVO(),
                    abcMap[it.id!!], ditMap[it.id!!], nocMap[it.id!!], lcom4Map[it.id!!]))
        }

        metricRepository.insertOrUpdateClassMetric(systemId, classMetrics)
        influxDBClient.save(ClassMetricsDtoListForWriteInfluxDB(classMetrics).toRequestBody())
    }
}

