package com.thoughtworks.archguard.report.domain.hub

import ModuleCouplingRepository
import com.thoughtworks.archguard.report.domain.ValidPagingParam.validPagingParam
import com.thoughtworks.archguard.report.domain.badsmell.BadSmellType
import com.thoughtworks.archguard.report.domain.coupling.ClassCouplingRepository
import com.thoughtworks.archguard.report.domain.coupling.MethodCouplingRepository
import com.thoughtworks.archguard.report.domain.coupling.PackageCouplingRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class HubService(val classCouplingRepository: ClassCouplingRepository,
                 val methodCouplingRepository: MethodCouplingRepository,
                 val packageCouplingRepository: PackageCouplingRepository,
                 val moduleCouplingRepository: ModuleCouplingRepository) {

    @Value("\${threshold.class.fanIn}")
    private val classFanInThreshold: Int = 0

    @Value("\${threshold.class.fanOut}")
    private val classFanOutThreshold: Int = 0

    @Value("\${threshold.method.fanIn}")
    private val methodFanInThreshold: Int = 0

    @Value("\${threshold.method.fanOut}")
    private val methodFanOutThreshold: Int = 0

    @Value("\${threshold.package.fanIn}")
    private val packageFanInThreshold: Int = 0

    @Value("\${threshold.package.fanOut}")
    private val packageFanOutThreshold: Int = 0

    @Value("\${threshold.module.fanIn}")
    private val moduleFanInThreshold: Int = 0

    @Value("\${threshold.module.fanOut}")
    private val moduleFanOutThreshold: Int = 0

    fun getClassHubListAboveThreshold(systemId: Long, limit: Long, offset: Long, orderByFanIn: Boolean): ClassHubListDto {
        validPagingParam(limit, offset)
        val classesCount = classCouplingRepository.getCouplingAboveThresholdCount(systemId, classFanInThreshold, classFanOutThreshold)
        val classesAboveThreshold = classCouplingRepository.getCouplingAboveThreshold(systemId, classFanInThreshold, classFanOutThreshold, offset, limit, orderByFanIn)
        return ClassHubListDto(classesAboveThreshold, classesCount, offset / limit + 1)
    }

    fun getMethodHubListAboveThreshold(systemId: Long, limit: Long, offset: Long, orderByFanIn: Boolean): MethodHubListDto {
        validPagingParam(limit, offset)
        val count = methodCouplingRepository.getCouplingAboveThresholdCount(systemId, methodFanInThreshold, methodFanOutThreshold)
        val methodsAboveThreshold = methodCouplingRepository.getCouplingAboveThreshold(systemId, methodFanInThreshold, methodFanOutThreshold, offset, limit, orderByFanIn)
        return MethodHubListDto(methodsAboveThreshold, count, offset / limit + 1)
    }

    fun getPackageHubListAboveThreshold(systemId: Long, limit: Long, offset: Long, orderByFanIn: Boolean): PackageHubListDto {
        validPagingParam(limit, offset)
        val count = packageCouplingRepository.getCouplingAboveThresholdCount(systemId, packageFanInThreshold, packageFanOutThreshold)
        val packagesAboveThreshold = packageCouplingRepository.getCouplingAboveThreshold(systemId, packageFanInThreshold, packageFanOutThreshold, offset, limit, orderByFanIn)
        return PackageHubListDto(packagesAboveThreshold, count, offset / limit + 1)
    }

    fun getModuleHubListAboveThreshold(systemId: Long, limit: Long, offset: Long, orderByFanIn: Boolean): ModuleHubListDto {
        validPagingParam(limit, offset)
        val count = moduleCouplingRepository.getCouplingAboveThresholdCount(systemId, moduleFanInThreshold, moduleFanOutThreshold)
        val modulesAboveThreshold = moduleCouplingRepository.getCouplingAboveThreshold(systemId, moduleFanInThreshold, moduleFanOutThreshold, offset, limit, orderByFanIn)
        return ModuleHubListDto(modulesAboveThreshold, count, offset / limit + 1)
    }

    fun getHubReport(systemId: Long): Map<BadSmellType, Long> {
        return mapOf(
                (BadSmellType.CLASSHUB to classCouplingRepository.getCouplingAboveThresholdCount(systemId, classFanInThreshold, classFanOutThreshold)),
                (BadSmellType.PACKAGEHUB to packageCouplingRepository.getCouplingAboveThresholdCount(systemId, packageFanInThreshold, packageFanOutThreshold)),
                (BadSmellType.MODULEHUB to moduleCouplingRepository.getCouplingAboveThresholdCount(systemId, moduleFanInThreshold, moduleFanOutThreshold)),
                (BadSmellType.METHODHUB to methodCouplingRepository.getCouplingAboveThresholdCount(systemId, methodFanInThreshold, methodFanOutThreshold))
        )
    }

}
