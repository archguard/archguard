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

    private fun getCircularDependencyWithTotalCount(systemId: Long, limit: Long, offset: Long,
                                                    type: CircularDependencyType): Triple<List<String>, Long, Int> {
        validPagingParam(limit, offset)
        val circularDependencyCount = circularDependencyRepository.getCircularDependencyCount(systemId, type)
        val circularDependencyList = circularDependencyRepository.getCircularDependency(systemId, type, limit, offset)
        return Triple(circularDependencyList, circularDependencyCount, 0)
    }

    fun getModuleCircularDependencyWithTotalCount(systemId: Long, limit: Long, offset: Long)
            : Triple<List<CircularDependency<ModuleVO>>, Long, Int> {
        val result = getCircularDependencyWithTotalCount(systemId, limit, offset, CircularDependencyType.MODULE)
        val data = result.first.map { CircularDependency(it.split(";").map { ModuleVO(it) }) }
        return Triple(data, result.second, result.third)
    }

    fun getPackageCircularDependencyWithTotalCount(systemId: Long, limit: Long, offset: Long)
            : Triple<List<CircularDependency<PackageVO>>, Long, Int> {
        val result = getCircularDependencyWithTotalCount(systemId, limit, offset, CircularDependencyType.PACKAGE)
        val data = result.first.map { CircularDependency(it.split(";").map { PackageVO.create(it) }) }
        return Triple(data, result.second, result.third)
    }

    fun getClassCircularDependencyWithTotalCount(systemId: Long, limit: Long, offset: Long)
            : Triple<List<CircularDependency<ClassVO>>, Long, Int> {
        val result = getCircularDependencyWithTotalCount(systemId, limit, offset, CircularDependencyType.CLASS)
        val data = result.first.map { CircularDependency(it.split(";").map { ClassVO.create(it) }) }
        return Triple(data, result.second, result.third)
    }

    fun getMethodCircularDependencyWithTotalCount(systemId: Long, limit: Long, offset: Long)
            : Triple<List<CircularDependency<MethodVO>>, Long, Int> {
        val result = getCircularDependencyWithTotalCount(systemId, limit, offset, CircularDependencyType.METHOD)
        val data = result.first.map { CircularDependency(it.split(";").map { MethodVO.create(it) }) }
        return Triple(data, result.second, result.third)
    }

    fun getCircularDependencyReport(systemId: Long): Map<BadSmellType, Long> {
        val sum = circularDependencyRepository.getCircularDependencyCount(systemId, CircularDependencyType.MODULE) +
                circularDependencyRepository.getCircularDependencyCount(systemId, CircularDependencyType.PACKAGE) +
                circularDependencyRepository.getCircularDependencyCount(systemId, CircularDependencyType.CLASS) +
                circularDependencyRepository.getCircularDependencyCount(systemId, CircularDependencyType.METHOD)
        return mapOf((BadSmellType.CYCLEDEPENDENCY to sum))
    }
}