package com.thoughtworks.archgard.scanner2.appl

import com.thoughtworks.archgard.scanner2.domain.model.CircularDependenciesCount
import com.thoughtworks.archgard.scanner2.domain.model.ClassMetric
import com.thoughtworks.archgard.scanner2.domain.repository.CircularDependencyMetricRepository
import com.thoughtworks.archgard.scanner2.domain.repository.ClassMetricRepository
import com.thoughtworks.archgard.scanner2.domain.repository.JClassRepository
import com.thoughtworks.archgard.scanner2.domain.service.AbcService
import com.thoughtworks.archgard.scanner2.domain.service.CircularDependencyService
import com.thoughtworks.archgard.scanner2.domain.service.DitService
import com.thoughtworks.archgard.scanner2.domain.service.FanInFanOutService
import com.thoughtworks.archgard.scanner2.domain.service.LCOM4Service
import com.thoughtworks.archgard.scanner2.domain.service.NocService
import com.thoughtworks.archgard.scanner2.infrastructure.influx.CircularDependenciesCountDtoForWriteInfluxDB
import com.thoughtworks.archgard.scanner2.infrastructure.influx.ClassMetricsDtoListForWriteInfluxDB
import com.thoughtworks.archgard.scanner2.infrastructure.influx.InfluxDBClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MetricPersistApplService(val abcService: AbcService,
                               val ditService: DitService,
                               val lcoM4Service: LCOM4Service,
                               val nocService: NocService,
                               val jClassRepository: JClassRepository,
                               val fanInFanOutService: FanInFanOutService,
                               val circularDependencyService: CircularDependencyService,
                               val classMetricRepository: ClassMetricRepository,
                               val circularDependencyMetricRepository: CircularDependencyMetricRepository,
                               val influxDBClient: InfluxDBClient) {
    private val log = LoggerFactory.getLogger(MetricPersistApplService::class.java)

    @Transactional
    fun persistLevel2Metrics(systemId: Long) {
        val jClasses = jClassRepository.getJClassesNotThirdParty(systemId)

        val abcMap = abcService.calculate(systemId, jClasses)
        val ditMap = ditService.calculate(systemId, jClasses)
        val nocMap = nocService.calculate(systemId, jClasses)
        val lcom4Map = lcoM4Service.calculate(systemId, jClasses)
        val hubMap = fanInFanOutService.calculateAtClassLevel(systemId, jClasses)

        val classMetrics = mutableListOf<ClassMetric>()
        jClasses.forEach {
            classMetrics.add(ClassMetric(systemId, it.toVO(),
                    abcMap[it.id], ditMap[it.id], nocMap[it.id], lcom4Map[it.id],
                    hubMap[it.id]?.fanIn, hubMap[it.id]?.fanOut))
        }

        classMetricRepository.insertOrUpdateClassMetric(systemId, classMetrics)
        influxDBClient.save(ClassMetricsDtoListForWriteInfluxDB(classMetrics).toRequestBody())
    }

    @Transactional
    fun persistCircularDependencyMetrics(systemId: Long) {
        val moduleCircularDependency = circularDependencyService.getModuleCircularDependency(systemId)
        circularDependencyMetricRepository.insertOrUpdateModuleCircularDependency(systemId, moduleCircularDependency)
        log.info("Finished persist moduleCircularDependency in projectId $systemId")

        val packageCircularDependency = circularDependencyService.getPackageCircularDependency(systemId)
        circularDependencyMetricRepository.insertOrUpdatePackageCircularDependency(systemId, packageCircularDependency)
        log.info("Finished persist packageCircularDependency in projectId $systemId")

        val classCircularDependency = circularDependencyService.getClassCircularDependency(systemId)
        circularDependencyMetricRepository.insertOrUpdateClassCircularDependency(systemId, classCircularDependency)
        log.info("Finished persist classCircularDependency in projectId $systemId")

        val methodCircularDependency = circularDependencyService.getMethodCircularDependency(systemId)
        circularDependencyMetricRepository.insertOrUpdateMethodCircularDependency(systemId, methodCircularDependency)
        log.info("Finished persist methodCircularDependency in projectId $systemId")

        val circularDependenciesCount = CircularDependenciesCount(systemId, moduleCircularDependency.size, packageCircularDependency.size, classCircularDependency.size, methodCircularDependency.size)
        influxDBClient.save(CircularDependenciesCountDtoForWriteInfluxDB(circularDependenciesCount).toRequestBody())
        log.info("Finished persist circularDependenciesCount to influxdb in projectId $systemId")

    }
}

