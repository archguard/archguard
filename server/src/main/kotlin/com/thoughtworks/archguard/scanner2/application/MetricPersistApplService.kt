package com.thoughtworks.archguard.scanner2.application

import com.thoughtworks.archguard.scanner2.domain.repository.ClassMetricRepository
import com.thoughtworks.archguard.scanner2.domain.repository.DataClassRepository
import com.thoughtworks.archguard.scanner2.domain.repository.JClassRepository
import com.thoughtworks.archguard.scanner2.domain.repository.JMethodRepository
import com.thoughtworks.archguard.scanner2.domain.repository.MethodMetricRepository
import com.thoughtworks.archguard.scanner2.domain.repository.ModuleMetricRepository
import com.thoughtworks.archguard.scanner2.domain.repository.PackageMetricRepository
import com.thoughtworks.archguard.scanner2.domain.repository.ScannerCircularDependencyMetricRepository
import com.thoughtworks.archguard.scanner2.domain.service.DitService
import com.thoughtworks.archguard.scanner2.domain.service.FanInFanOutService
import com.thoughtworks.archguard.scanner2.domain.service.LCOM4Service
import com.thoughtworks.archguard.scanner2.domain.service.NocService
import com.thoughtworks.archguard.scanner2.domain.service.ScannerCircularDependencyServiceImpl
import com.thoughtworks.archguard.scanner2.domain.service.ScannerDataClassService
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.archguard.metric.ModuleMetric
import org.archguard.metric.PackageMetric
import org.archguard.metric.ClassMetric
import org.archguard.model.vos.JClassVO
import org.archguard.model.vos.JMethodVO
import org.archguard.metric.MethodMetric
import org.archguard.model.code.JClass
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MetricPersistApplService(
    val ditService: DitService,
    val lcoM4Service: LCOM4Service,
    val nocService: NocService,
    val dataClassService: ScannerDataClassService,
    val jClassRepository: JClassRepository,
    val jMethodRepository: JMethodRepository,
    val fanInFanOutService: FanInFanOutService,
    val scannerCircularDependencyServiceImpl: ScannerCircularDependencyServiceImpl,
    val classMetricRepository: ClassMetricRepository,
    val methodMetricRepository: MethodMetricRepository,
    val packageMetricRepository: PackageMetricRepository,
    val moduleMetricRepository: ModuleMetricRepository,
    val dataClassRepository: DataClassRepository,
    val circularDependencyMetricRepository: ScannerCircularDependencyMetricRepository
) {
    private val log = LoggerFactory.getLogger(MetricPersistApplService::class.java)

    fun persistLevel2Metrics(systemId: Long) {
        log.info("**************************************************************************")
        log.info(" Begin calculate and persist Level 2 Metric in systemId $systemId")
        log.info("**************************************************************************")
        val jClasses = jClassRepository.getJClassesNotThirdPartyAndNotTest(systemId)
        runBlocking {
            val classJob = launch { persistClassLevel2Metrics(systemId, jClasses) }
            val methodJob = launch { persistMethodLevel2Metrics(systemId) }
            val packageJob = launch { persistPackageLevel2Metrics(systemId, jClasses) }
            val moduleJob = launch { persistModuleLevel2Metrics(systemId, jClasses) }
            classJob.join()
            methodJob.join()
            packageJob.join()
            moduleJob.join()
        }
    }

    @Transactional
    fun persistDataClass(systemId: Long) {
        val dataClasses = dataClassService.findAllDataClasses(systemId)
        dataClassRepository.insertOrUpdateDataClass(systemId, dataClasses)
        log.info("-----------------------------------------------------------------------")
        log.info("Finished persist data class Metric for systemId $systemId")
        log.info("-----------------------------------------------------------------------")
    }

    @Transactional
    fun persistCircularDependencyMetrics(systemId: Long) {
        val moduleCircularDependency = scannerCircularDependencyServiceImpl.getModuleCircularDependency(systemId)
        circularDependencyMetricRepository.insertOrUpdateModuleCircularDependency(systemId, moduleCircularDependency)
        log.info("Finished persist moduleCircularDependency in systemId $systemId")

        val packageCircularDependency = scannerCircularDependencyServiceImpl.getPackageCircularDependency(systemId)
        circularDependencyMetricRepository.insertOrUpdatePackageCircularDependency(systemId, packageCircularDependency)
        log.info("Finished persist packageCircularDependency in systemId $systemId")

        val classCircularDependency = scannerCircularDependencyServiceImpl.getClassCircularDependency(systemId)
        circularDependencyMetricRepository.insertOrUpdateClassCircularDependency(systemId, classCircularDependency)
        log.info("Finished persist classCircularDependency in systemId $systemId")

        val methodCircularDependency = scannerCircularDependencyServiceImpl.getMethodCircularDependency(systemId)
        circularDependencyMetricRepository.insertOrUpdateMethodCircularDependency(systemId, methodCircularDependency)
        log.info("Finished persist methodCircularDependency in systemId $systemId")

        log.info("-----------------------------------------------------------------------")
        log.info("Finished persist circularDependenciesCount for systemId $systemId")
        log.info("-----------------------------------------------------------------------")
    }

    private suspend fun persistModuleLevel2Metrics(systemId: Long, jClasses: List<JClass>) {
        val moduleFanInFanOutMap = fanInFanOutService.calculateAtModuleLevel(systemId, jClasses)
        val moduleMetrics = moduleFanInFanOutMap.map {
            ModuleMetric(systemId, it.key, it.value.fanIn, it.value.fanOut)
        }
        log.info("Finished calculate moduleMetric in systemId $systemId")

        moduleMetricRepository.insertOrUpdateModuleMetric(systemId, moduleMetrics)

        log.info("-----------------------------------------------------------------------")
        log.info("Finished persist module Metric to mysql for systemId $systemId")
        log.info("-----------------------------------------------------------------------")
    }

    private suspend fun persistPackageLevel2Metrics(systemId: Long, jClasses: List<JClass>) {
        val packageFanInFanOutMap = fanInFanOutService.calculateAtPackageLevel(systemId, jClasses)
        val packageMetrics = packageFanInFanOutMap.map {
            PackageMetric(
                systemId, getModuleNameFromPackageFullName(it.key), getPackageNameFromPackageFullName(it.key),
                it.value.fanIn, it.value.fanOut
            )
        }
        log.info("Finished calculate packageMetric in systemId $systemId")
        packageMetricRepository.insertOrUpdatePackageMetric(systemId, packageMetrics)

        log.info("-----------------------------------------------------------------------")
        log.info("Finished persist package Metric to mysql for systemId $systemId")
        log.info("-----------------------------------------------------------------------")
    }

    private suspend fun persistMethodLevel2Metrics(systemId: Long) {
        val methods = jMethodRepository.getMethodsNotThirdPartyAndNotTest(systemId)
        val methodFanInFanOutMap = fanInFanOutService.calculateAtMethodLevel(systemId)

        val methodMetrics = methods.map {
            MethodMetric(
                systemId, JMethodVO.fromJMethod(it),
                methodFanInFanOutMap[it.id]?.fanIn ?: 0, methodFanInFanOutMap[it.id]?.fanOut ?: 0
            )
        }
        log.info("Finished calculate methodMetric in systemId $systemId")

        methodMetricRepository.insertOrUpdateMethodMetric(systemId, methodMetrics)

        log.info("-----------------------------------------------------------------------")
        log.info("Finished persist method Metric to mysql for in systemId $systemId")
        log.info("-----------------------------------------------------------------------")
    }

    private suspend fun persistClassLevel2Metrics(systemId: Long, jClasses: List<JClass>) = coroutineScope {
        val ditMap = async { ditService.calculate(systemId, jClasses) }
        val nocMap = async { nocService.calculate(systemId, jClasses) }
        val lcom4Map = async { lcoM4Service.calculate(systemId, jClasses) }
        val classFanInFanOutMap = async { fanInFanOutService.calculateAtClassLevel(systemId) }

        val classMetrics = jClasses.map {
            ClassMetric(
                systemId, JClassVO.fromClass(it),
                ditMap.await()[it.id],
                nocMap.await()[it.id],
                lcom4Map.await()[it.id],
                classFanInFanOutMap.await()[it.id]?.fanIn ?: 0,
                classFanInFanOutMap.await()[it.id]?.fanOut ?: 0
            )
        }
        classMetricRepository.insertOrUpdateClassMetric(systemId, classMetrics)
        log.info("-----------------------------------------------------------------------")
        log.info("Finished persist class Metric to mysql for systemId $systemId")
        log.info("-----------------------------------------------------------------------")
    }
}
