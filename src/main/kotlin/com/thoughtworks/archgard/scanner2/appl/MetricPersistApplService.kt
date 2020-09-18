package com.thoughtworks.archgard.scanner2.appl

import com.thoughtworks.archgard.scanner2.domain.model.CircularDependenciesCount
import com.thoughtworks.archgard.scanner2.domain.model.ClassMetric
import com.thoughtworks.archgard.scanner2.domain.model.MethodMetric
import com.thoughtworks.archgard.scanner2.domain.model.ModuleMetric
import com.thoughtworks.archgard.scanner2.domain.model.PackageMetric
import com.thoughtworks.archgard.scanner2.domain.repository.CircularDependencyMetricRepository
import com.thoughtworks.archgard.scanner2.domain.repository.ClassMetricRepository
import com.thoughtworks.archgard.scanner2.domain.repository.JClassRepository
import com.thoughtworks.archgard.scanner2.domain.repository.JMethodRepository
import com.thoughtworks.archgard.scanner2.domain.repository.MethodMetricRepository
import com.thoughtworks.archgard.scanner2.domain.repository.ModuleMetricRepository
import com.thoughtworks.archgard.scanner2.domain.repository.PackageMetricRepository
import com.thoughtworks.archgard.scanner2.domain.service.AbcService
import com.thoughtworks.archgard.scanner2.domain.service.CircularDependencyService
import com.thoughtworks.archgard.scanner2.domain.service.DitService
import com.thoughtworks.archgard.scanner2.domain.service.FanInFanOutService
import com.thoughtworks.archgard.scanner2.domain.service.LCOM4Service
import com.thoughtworks.archgard.scanner2.domain.service.NocService
import com.thoughtworks.archgard.scanner2.infrastructure.influx.CircularDependenciesCountDtoForWriteInfluxDB
import com.thoughtworks.archgard.scanner2.infrastructure.influx.ClassMetricsDtoListForWriteInfluxDB
import com.thoughtworks.archgard.scanner2.infrastructure.influx.InfluxDBClient
import com.thoughtworks.archgard.scanner2.infrastructure.influx.MethodMetricsDtoListForWriteInfluxDB
import com.thoughtworks.archgard.scanner2.infrastructure.influx.ModuleMetricsDtoListForWriteInfluxDB
import com.thoughtworks.archgard.scanner2.infrastructure.influx.PackageMetricsDtoListForWriteInfluxDB
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MetricPersistApplService(val abcService: AbcService,
                               val ditService: DitService,
                               val lcoM4Service: LCOM4Service,
                               val nocService: NocService,
                               val jClassRepository: JClassRepository,
                               val jMethodRepository: JMethodRepository,
                               val fanInFanOutService: FanInFanOutService,
                               val circularDependencyService: CircularDependencyService,
                               val classMetricRepository: ClassMetricRepository,
                               val methodMetricRepository: MethodMetricRepository,
                               val packageMetricRepository: PackageMetricRepository,
                               val moduleMetricRepository: ModuleMetricRepository,
                               val circularDependencyMetricRepository: CircularDependencyMetricRepository,
                               val influxDBClient: InfluxDBClient) {
    private val log = LoggerFactory.getLogger(MetricPersistApplService::class.java)

    @Transactional
    fun persistLevel2Metrics(systemId: Long) {

        val jClasses = jClassRepository.getJClassesNotThirdParty(systemId)

        log.info("begin calculate Metric in systemId $systemId")

        val abcMap = abcService.calculate(systemId, jClasses)
        val ditMap = ditService.calculate(systemId, jClasses)
        val nocMap = nocService.calculate(systemId, jClasses)
        val lcom4Map = lcoM4Service.calculate(systemId, jClasses)
        val classFanInFanOutMap = fanInFanOutService.calculateAtClassLevel(systemId)

        val classMetrics = jClasses.map {
            ClassMetric(systemId, it.toVO(),
                    abcMap[it.id], ditMap[it.id], nocMap[it.id], lcom4Map[it.id],
                    classFanInFanOutMap[it.id]?.fanIn, classFanInFanOutMap[it.id]?.fanOut)
        }
        log.info("Finished calculate classMetric in systemId $systemId")

        val methods = jMethodRepository.getMethodsNotThirdParty(systemId)
        val methodFanInFanOutMap = fanInFanOutService.calculateAtMethodLevel(systemId)

        val methodMetrics = methods.map {
            MethodMetric(systemId, it.toVO(),
                    methodFanInFanOutMap[it.id]?.fanIn, methodFanInFanOutMap[it.id]?.fanOut)
        }
        log.info("Finished calculate methodMetric in systemId $systemId")

        val packageFanInFanOutMap = fanInFanOutService.calculateAtPackageLevel(systemId, jClasses)
        val packageMetrics = packageFanInFanOutMap.map {
            PackageMetric(systemId, getModuleNameFromPackageFullName(it.key), getPackageNameFromPackageFullName(it.key),
                    it.value.fanIn, it.value.fanOut)
        }
        log.info("Finished calculate packageMetric in systemId $systemId")

        val moduleFanInFanOutMap = fanInFanOutService.calculateAtModuleLevel(systemId, jClasses)
        val moduleMetrics = moduleFanInFanOutMap.map {
            ModuleMetric(systemId, it.key, it.value.fanIn, it.value.fanOut)
        }
        log.info("Finished calculate moduleMetric in systemId $systemId")

        classMetricRepository.insertOrUpdateClassMetric(systemId, classMetrics)
        methodMetricRepository.insertOrUpdateMethodMetric(systemId, methodMetrics)
        packageMetricRepository.insertOrUpdateMethodMetric(systemId, packageMetrics)
        moduleMetricRepository.insertOrUpdateModuleMetric(systemId, moduleMetrics)
        log.info("Finished persist class, method, package, module Metric to mysql in systemId $systemId")

        influxDBClient.save(ClassMetricsDtoListForWriteInfluxDB(classMetrics).toRequestBody())
        influxDBClient.save(MethodMetricsDtoListForWriteInfluxDB(methodMetrics).toRequestBody())
        influxDBClient.save(PackageMetricsDtoListForWriteInfluxDB(packageMetrics).toRequestBody())
        influxDBClient.save(ModuleMetricsDtoListForWriteInfluxDB(moduleMetrics).toRequestBody())
        log.info("Finished persist class, method, package, module Metric to influxdb in systemId $systemId")
    }

    @Transactional
    fun persistCircularDependencyMetrics(systemId: Long) {
        val moduleCircularDependency = circularDependencyService.getModuleCircularDependency(systemId)
        circularDependencyMetricRepository.insertOrUpdateModuleCircularDependency(systemId, moduleCircularDependency)
        log.info("Finished persist moduleCircularDependency in systemId $systemId")

        val packageCircularDependency = circularDependencyService.getPackageCircularDependency(systemId)
        circularDependencyMetricRepository.insertOrUpdatePackageCircularDependency(systemId, packageCircularDependency)
        log.info("Finished persist packageCircularDependency in systemId $systemId")

        val classCircularDependency = circularDependencyService.getClassCircularDependency(systemId)
        circularDependencyMetricRepository.insertOrUpdateClassCircularDependency(systemId, classCircularDependency)
        log.info("Finished persist classCircularDependency in systemId $systemId")

        val methodCircularDependency = circularDependencyService.getMethodCircularDependency(systemId)
        circularDependencyMetricRepository.insertOrUpdateMethodCircularDependency(systemId, methodCircularDependency)
        log.info("Finished persist methodCircularDependency in systemId $systemId")

        val circularDependenciesCount = CircularDependenciesCount(systemId, moduleCircularDependency.size, packageCircularDependency.size, classCircularDependency.size, methodCircularDependency.size)
        influxDBClient.save(CircularDependenciesCountDtoForWriteInfluxDB(circularDependenciesCount).toRequestBody())
        log.info("Finished persist circularDependenciesCount to influxdb in systemId $systemId")

    }
}

fun getModuleNameFromPackageFullName(fullName: String): String {
    return fullName.substring(0, fullName.indexOfFirst { it == '-' })
}

fun getPackageNameFromPackageFullName(fullName: String): String {
    return fullName.substring(fullName.indexOfFirst { it == '-' })
}
