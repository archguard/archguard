package com.thoughtworks.archguard.report.domain.hub

import ModuleCouplingRepository
import com.thoughtworks.archguard.report.domain.ValidPagingParam.validPagingParam
import com.thoughtworks.archguard.report.domain.coupling.ClassCouplingRepository
import com.thoughtworks.archguard.report.domain.coupling.MethodCouplingRepository
import com.thoughtworks.archguard.report.domain.coupling.PackageCouplingRepository
import org.springframework.stereotype.Service

@Service
class HubService(val classCouplingRepository: ClassCouplingRepository,
                 val methodCouplingRepository: MethodCouplingRepository,
                 val packageCouplingRepository: PackageCouplingRepository,
                 val moduleCouplingRepository: ModuleCouplingRepository) {
    fun getClassHubListAboveThreshold(systemId: Long, classFanInThreshold: Int, classFanOutThreshold: Int, limit: Long, offset: Long, orderByFanIn: Boolean): ClassHubListDto {
        validPagingParam(limit, offset)
        val classesCount = classCouplingRepository.getCouplingAboveThresholdCount(systemId, classFanInThreshold, classFanOutThreshold)
        val classesAboveThreshold = classCouplingRepository.getCouplingAboveThreshold(systemId, classFanInThreshold, classFanOutThreshold, offset, limit, orderByFanIn)
        return ClassHubListDto(classesAboveThreshold, classesCount, offset / limit + 1)
    }

    fun getMethodHubListAboveThreshold(systemId: Long, methodFanInThreshold: Int, methodFanOutThreshold: Int, limit: Long, offset: Long, orderByFanIn: Boolean): MethodHubListDto {
        validPagingParam(limit, offset)
        val count = methodCouplingRepository.getCouplingAboveThresholdCount(systemId, methodFanInThreshold, methodFanOutThreshold)
        val methodsAboveThreshold = methodCouplingRepository.getCouplingAboveThreshold(systemId, methodFanInThreshold, methodFanOutThreshold, offset, limit, orderByFanIn)
        return MethodHubListDto(methodsAboveThreshold, count, offset / limit + 1)
    }

    fun getPackageHubListAboveThreshold(systemId: Long, packageFanInThreshold: Int, packageFanOutThreshold: Int, limit: Long, offset: Long, orderByFanIn: Boolean): PackageHubListDto {
        validPagingParam(limit, offset)
        val count = packageCouplingRepository.getCouplingAboveThresholdCount(systemId, packageFanInThreshold, packageFanOutThreshold)
        val packagesAboveThreshold = packageCouplingRepository.getCouplingAboveThreshold(systemId, packageFanInThreshold, packageFanOutThreshold, offset, limit, orderByFanIn)
        return PackageHubListDto(packagesAboveThreshold, count, offset / limit + 1)
    }

    fun getModuleHubListAboveThreshold(systemId: Long, moduleFanInThreshold: Int, moduleFanOutThreshold: Int, limit: Long, offset: Long, orderByFanIn: Boolean): ModuleHubListDto {
        validPagingParam(limit, offset)
        val count = moduleCouplingRepository.getCouplingAboveThresholdCount(systemId, moduleFanInThreshold, moduleFanOutThreshold)
        val modulesAboveThreshold = moduleCouplingRepository.getCouplingAboveThreshold(systemId, moduleFanInThreshold, moduleFanOutThreshold, offset, limit, orderByFanIn)
        return ModuleHubListDto(modulesAboveThreshold, count, offset / limit + 1)
    }

}
