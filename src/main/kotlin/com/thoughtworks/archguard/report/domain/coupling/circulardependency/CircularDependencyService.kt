package com.thoughtworks.archguard.report.domain.coupling.circulardependency

import com.thoughtworks.archguard.report.domain.ValidPagingParam.validPagingParam
import com.thoughtworks.archguard.report.domain.badsmell.BadSmellType
import com.thoughtworks.archguard.report.domain.models.ClassVO
import com.thoughtworks.archguard.report.domain.models.MethodVO
import com.thoughtworks.archguard.report.domain.models.ModuleVO
import com.thoughtworks.archguard.report.domain.models.PackageVO
import org.springframework.stereotype.Service

@Service
class CircularDependencyService(val circularDependencyRepository: CircularDependencyRepository) {
    private fun getCircularDependencyWithTotalCount(systemId: Long, limit: Long, offset: Long, type: CircularDependencyType): CircularDependencyStringListDto {
        validPagingParam(limit, offset)
        val circularDependencyCount = circularDependencyRepository.getCircularDependencyCount(systemId, type)
        val circularDependencyList = circularDependencyRepository.getCircularDependency(systemId, type, limit, offset)
        return CircularDependencyStringListDto(circularDependencyList,
                circularDependencyCount,
                offset / limit + 1)
    }

    fun getModuleCircularDependencyWithTotalCount(systemId: Long, limit: Long, offset: Long): CircularDependencyListDto<ModuleVO> {
        val circularDependencyWithTotalCount = getCircularDependencyWithTotalCount(systemId, limit, offset, CircularDependencyType.MODULE)
        val data = circularDependencyWithTotalCount.data.map { CircularDependency(it.split(";").map { ModuleVO(it) }) }
        return CircularDependencyListDto(data, circularDependencyWithTotalCount.count, circularDependencyWithTotalCount.currentPageNumber)
    }

    fun getPackageCircularDependencyWithTotalCount(systemId: Long, limit: Long, offset: Long): CircularDependencyListDto<PackageVO> {
        val circularDependencyWithTotalCount = getCircularDependencyWithTotalCount(systemId, limit, offset, CircularDependencyType.PACKAGE)
        val data = circularDependencyWithTotalCount.data.map { CircularDependency(it.split(";").map { PackageVO.create(it) }) }
        return CircularDependencyListDto(data, circularDependencyWithTotalCount.count, circularDependencyWithTotalCount.currentPageNumber)
    }

    fun getClassCircularDependencyWithTotalCount(systemId: Long, limit: Long, offset: Long): CircularDependencyListDto<ClassVO> {
        val circularDependencyWithTotalCount = getCircularDependencyWithTotalCount(systemId, limit, offset, CircularDependencyType.CLASS)
        val data = circularDependencyWithTotalCount.data.map { CircularDependency(it.split(";").map { ClassVO.create(it) }) }
        return CircularDependencyListDto(data, circularDependencyWithTotalCount.count, circularDependencyWithTotalCount.currentPageNumber)
    }

    fun getMethodCircularDependencyWithTotalCount(systemId: Long, limit: Long, offset: Long): CircularDependencyListDto<MethodVO> {
        val circularDependencyWithTotalCount = getCircularDependencyWithTotalCount(systemId, limit, offset, CircularDependencyType.METHOD)
        val data = circularDependencyWithTotalCount.data.map { CircularDependency(it.split(";").map { MethodVO.create(it) }) }
        return CircularDependencyListDto(data, circularDependencyWithTotalCount.count, circularDependencyWithTotalCount.currentPageNumber)
    }

    fun getCircularDependencyReport(systemId: Long): Map<BadSmellType, Long> {
        val sum = circularDependencyRepository.getCircularDependencyCount(systemId, CircularDependencyType.MODULE) +
                circularDependencyRepository.getCircularDependencyCount(systemId, CircularDependencyType.PACKAGE) +
                circularDependencyRepository.getCircularDependencyCount(systemId, CircularDependencyType.CLASS) +
                circularDependencyRepository.getCircularDependencyCount(systemId, CircularDependencyType.METHOD)
        return mapOf((BadSmellType.CYCLEDEPENDENCY to sum))
    }
}