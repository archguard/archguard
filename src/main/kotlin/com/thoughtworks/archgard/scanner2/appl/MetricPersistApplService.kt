package com.thoughtworks.archgard.scanner2.appl

import com.thoughtworks.archgard.scanner2.common.Scanner2ThreadPool
import com.thoughtworks.archgard.scanner2.domain.model.CircularDependenciesCount
import com.thoughtworks.archgard.scanner2.domain.model.ClassMetric
import com.thoughtworks.archgard.scanner2.domain.model.JClass
import com.thoughtworks.archgard.scanner2.domain.model.MethodMetric
import com.thoughtworks.archgard.scanner2.domain.model.ModuleMetric
import com.thoughtworks.archgard.scanner2.domain.model.PackageMetric
import com.thoughtworks.archgard.scanner2.domain.repository.CircularDependencyMetricRepository
import com.thoughtworks.archgard.scanner2.domain.repository.ClassMetricRepository
import com.thoughtworks.archgard.scanner2.domain.repository.DataClassRepository
import com.thoughtworks.archgard.scanner2.domain.repository.JClassRepository
import com.thoughtworks.archgard.scanner2.domain.repository.JMethodRepository
import com.thoughtworks.archgard.scanner2.domain.repository.MethodMetricRepository
import com.thoughtworks.archgard.scanner2.domain.repository.ModuleMetricRepository
import com.thoughtworks.archgard.scanner2.domain.repository.PackageMetricRepository
import com.thoughtworks.archgard.scanner2.domain.service.AbcService
import com.thoughtworks.archgard.scanner2.domain.service.CircularDependencyService
import com.thoughtworks.archgard.scanner2.domain.service.DataClassService
import com.thoughtworks.archgard.scanner2.domain.service.DitService
import com.thoughtworks.archgard.scanner2.domain.service.FanInFanOut
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
import java.util.concurrent.CountDownLatch


@Service
class MetricPersistApplService(val abcService: AbcService,
                               val ditService: DitService,
                               val lcoM4Service: LCOM4Service,
                               val nocService: NocService,
                               val dataClassService: DataClassService,
                               val jClassRepository: JClassRepository,
                               val jMethodRepository: JMethodRepository,
                               val fanInFanOutService: FanInFanOutService,
                               val circularDependencyService: CircularDependencyService,
                               val classMetricRepository: ClassMetricRepository,
                               val methodMetricRepository: MethodMetricRepository,
                               val packageMetricRepository: PackageMetricRepository,
                               val moduleMetricRepository: ModuleMetricRepository,
                               val dataClassRepository: DataClassRepository,
                               val circularDependencyMetricRepository: CircularDependencyMetricRepository,
                               val influxDBClient: InfluxDBClient,
                               val scanner2ThreadPool: Scanner2ThreadPool) {
    private val log = LoggerFactory.getLogger(MetricPersistApplService::class.java)

    fun persistLevel2Metrics(systemId: Long) {

        val jClasses = jClassRepository.getJClassesNotThirdParty(systemId)

        log.info("begin calculate and persist Metric in systemId $systemId")
        scanner2ThreadPool.submit(Runnable {
            persistClassLevel2Metrics(systemId, jClasses)
        })
        scanner2ThreadPool.submit(Runnable {
            persistMethodLevel2Metrics(systemId)
        })
        scanner2ThreadPool.submit(Runnable {
            persistPackageLevel2Metrics(systemId, jClasses)
        })
        scanner2ThreadPool.submit(Runnable {
            persistModuleLevel2Metrics(systemId, jClasses)
        })
    }

    @Transactional
    fun persistDataClass(systemId: Long) {
        dataClassRepository.insertOrUpdateDataClass(systemId, dataClassService.findAllDataClasses(systemId))
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

    private fun persistModuleLevel2Metrics(systemId: Long, jClasses: List<JClass>) {
        val moduleFanInFanOutMap = fanInFanOutService.calculateAtModuleLevel(systemId, jClasses)
        val moduleMetrics = moduleFanInFanOutMap.map {
            ModuleMetric(systemId, it.key, it.value.fanIn, it.value.fanOut)
        }
        log.info("Finished calculate moduleMetric in systemId $systemId")

        moduleMetricRepository.insertOrUpdateModuleMetric(systemId, moduleMetrics)
        influxDBClient.save(ModuleMetricsDtoListForWriteInfluxDB(moduleMetrics).toRequestBody())
        log.info("Finished persist module Metric to mysql and influxdb in systemId $systemId")
    }

    private fun persistPackageLevel2Metrics(systemId: Long, jClasses: List<JClass>) {
        val packageFanInFanOutMap = fanInFanOutService.calculateAtPackageLevel(systemId, jClasses)
        val packageMetrics = packageFanInFanOutMap.map {
            PackageMetric(systemId, getModuleNameFromPackageFullName(it.key), getPackageNameFromPackageFullName(it.key),
                    it.value.fanIn, it.value.fanOut)
        }
        log.info("Finished calculate packageMetric in systemId $systemId")
        packageMetricRepository.insertOrUpdatePackageMetric(systemId, packageMetrics)
        influxDBClient.save(PackageMetricsDtoListForWriteInfluxDB(packageMetrics).toRequestBody())
        log.info("Finished persist package Metric to mysql and influxdb in systemId $systemId")
    }

    private fun persistMethodLevel2Metrics(systemId: Long) {
        val methods = jMethodRepository.getMethodsNotThirdParty(systemId)
        val methodFanInFanOutMap = fanInFanOutService.calculateAtMethodLevel(systemId)

        val methodMetrics = methods.map {
            MethodMetric(systemId, it.toVO(),
                    methodFanInFanOutMap[it.id]?.fanIn ?: 0, methodFanInFanOutMap[it.id]?.fanOut ?: 0)
        }
        log.info("Finished calculate methodMetric in systemId $systemId")

        methodMetricRepository.insertOrUpdateMethodMetric(systemId, methodMetrics)
        influxDBClient.save(MethodMetricsDtoListForWriteInfluxDB(methodMetrics).toRequestBody())
        log.info("Finished persist method Metric to mysql and influxdb in systemId $systemId")
    }

    private fun persistClassLevel2Metrics(systemId: Long, jClasses: List<JClass>) {
        val latch = CountDownLatch(5)
        var abcMap = emptyMap<String, Int>()
        var ditMap = emptyMap<String, Int>()
        var nocMap = emptyMap<String, Int>()
        var lcom4Map = emptyMap<String, Int>()
        var classFanInFanOutMap = emptyMap<String, FanInFanOut>()
        scanner2ThreadPool.submit(Runnable {
            abcMap = abcService.calculate(systemId, jClasses)
            latch.countDown()
        })
        scanner2ThreadPool.submit(Runnable {
            ditMap = ditService.calculate(systemId, jClasses)
            latch.countDown()
        })
        scanner2ThreadPool.submit(Runnable {
            nocMap = nocService.calculate(systemId, jClasses)
            latch.countDown()
        })
        scanner2ThreadPool.submit(Runnable {
            lcom4Map = lcoM4Service.calculate(systemId, jClasses)
            latch.countDown()
        })
        scanner2ThreadPool.submit(Runnable {
            classFanInFanOutMap = fanInFanOutService.calculateAtClassLevel(systemId)
            latch.countDown()
        })
        latch.await()

        val classMetrics = jClasses.map {
            ClassMetric(systemId, it.toVO(),
                    abcMap[it.id], ditMap[it.id], nocMap[it.id], lcom4Map[it.id],
                    classFanInFanOutMap[it.id]?.fanIn ?: 0, classFanInFanOutMap[it.id]?.fanOut ?: 0)
        }
        log.info("Finished calculate classMetric in systemId $systemId")

        classMetricRepository.insertOrUpdateClassMetric(systemId, classMetrics)
        influxDBClient.save(ClassMetricsDtoListForWriteInfluxDB(classMetrics).toRequestBody())
        log.info("Finished persist class Metric to mysql and influxdb in systemId $systemId")
    }
}
