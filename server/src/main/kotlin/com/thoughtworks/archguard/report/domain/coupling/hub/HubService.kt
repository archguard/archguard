package com.thoughtworks.archguard.report.domain.coupling.hub

import com.thoughtworks.archguard.report.domain.ValidPagingParam.validPagingParam
import org.archguard.smell.BadSmellType
import org.archguard.threshold.ThresholdKey
import com.thoughtworks.archguard.report.domain.badsmell.ThresholdSuiteService
import org.springframework.stereotype.Service

@Service
class HubService(
    val thresholdSuiteService: ThresholdSuiteService,
    val classCouplingRepository: ClassCouplingRepository,
    val methodCouplingRepository: MethodCouplingRepository,
    val packageCouplingRepository: PackageCouplingRepository,
    val moduleCouplingRepository: ModuleCouplingRepository
) {

    fun getClassHubListAboveThreshold(systemId: Long, limit: Long, offset: Long, orderByFanIn: Boolean): Triple<List<ClassCoupling>, Long, Int> {
        validPagingParam(limit, offset)
        val threshold = thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.COUPLING_HUB_CLASS)
        val classesCount = classCouplingRepository.getCouplingAboveThresholdCount(systemId, threshold, threshold)
        val classesAboveThreshold = classCouplingRepository.getCouplingAboveThreshold(systemId, threshold, threshold, offset, limit, orderByFanIn)
        return Triple(classesAboveThreshold, classesCount, threshold)
    }

    fun getMethodHubListAboveThreshold(systemId: Long, limit: Long, offset: Long, orderByFanIn: Boolean): Triple<List<MethodCoupling>, Long, Int> {
        validPagingParam(limit, offset)
        val threshold = thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.COUPLING_HUB_METHOD)
        val count = methodCouplingRepository.getCouplingAboveThresholdCount(systemId, threshold, threshold)
        val methodsAboveThreshold = methodCouplingRepository.getCouplingAboveThreshold(systemId, threshold, threshold, offset, limit, orderByFanIn)
        return Triple(methodsAboveThreshold, count, threshold)
    }

    fun getPackageHubListAboveThreshold(systemId: Long, limit: Long, offset: Long, orderByFanIn: Boolean): Triple<List<PackageCoupling>, Long, Int> {
        validPagingParam(limit, offset)
        val threshold = thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.COUPLING_HUB_PACKAGE)
        val count = packageCouplingRepository.getCouplingAboveThresholdCount(systemId, threshold, threshold)
        val packagesAboveThreshold = packageCouplingRepository.getCouplingAboveThreshold(systemId, threshold, threshold, offset, limit, orderByFanIn)
        return Triple(packagesAboveThreshold, count, threshold)
    }

    fun getModuleHubListAboveThreshold(systemId: Long, limit: Long, offset: Long, orderByFanIn: Boolean): Triple<List<ModuleCoupling>, Long, Int> {
        validPagingParam(limit, offset)
        val threshold = thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.COUPLING_HUB_MODULE)
        val count = moduleCouplingRepository.getCouplingAboveThresholdCount(systemId, threshold, threshold)
        val modulesAboveThreshold = moduleCouplingRepository.getCouplingAboveThreshold(systemId, threshold, threshold, offset, limit, orderByFanIn)
        return Triple(modulesAboveThreshold, count, threshold)
    }

    fun getHubReport(systemId: Long): Map<BadSmellType, Long> {
        val moduleThreshold = thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.COUPLING_HUB_MODULE)
        val packageThreshold = thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.COUPLING_HUB_PACKAGE)
        val classThreshold = thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.COUPLING_HUB_CLASS)
        val methodThreshold = thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.COUPLING_HUB_METHOD)
        return mapOf(
            (BadSmellType.CLASSHUB to classCouplingRepository.getCouplingAboveThresholdCount(systemId, classThreshold, classThreshold)),
            (BadSmellType.PACKAGEHUB to packageCouplingRepository.getCouplingAboveThresholdCount(systemId, packageThreshold, packageThreshold)),
            (BadSmellType.MODULEHUB to moduleCouplingRepository.getCouplingAboveThresholdCount(systemId, moduleThreshold, moduleThreshold)),
            (BadSmellType.METHODHUB to methodCouplingRepository.getCouplingAboveThresholdCount(systemId, methodThreshold, methodThreshold))
        )
    }
}
