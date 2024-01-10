package com.thoughtworks.archguard.report.domain.overview.calculator

import com.thoughtworks.archguard.report.domain.coupling.circulardependency.CircularDependencyRepository
import com.thoughtworks.archguard.report.domain.coupling.circulardependency.CircularDependencyType
import org.archguard.smell.BadSmellLevel
import org.archguard.smell.BadSmellLevelCalculator
import org.springframework.stereotype.Component

@Component
class CircularDependencyCalculator(val circularDependencyRepository: CircularDependencyRepository) :
    BadSmellLevelCalculator {
    override fun getCalculateResult(systemId: Long): BadSmellLevel {
        val methodResult = circularDependencyRepository
            .getCircularDependencyBadSmellCalculateResult(
                systemId,
                CircularDependencyType.METHOD,
                getMethodCircularDependencyLevelRanges()
            )
        val classResult = circularDependencyRepository
            .getCircularDependencyBadSmellCalculateResult(
                systemId,
                CircularDependencyType.CLASS,
                getClassCircularDependencyLevelRanges()
            )
        val packageResult = circularDependencyRepository
            .getCircularDependencyBadSmellCalculateResult(
                systemId,
                CircularDependencyType.PACKAGE,
                getPackageCircularDependencyLevelRanges()
            )

        val moduleResult = circularDependencyRepository
            .getCircularDependencyBadSmellCalculateResult(
                systemId,
                CircularDependencyType.MODULE,
                getModuleCircularDependencyLevelRanges()
            )

        var result = methodResult.plus(classResult)
        result = result.plus(packageResult)
        result = result.plus(moduleResult)
        return result
    }

    fun getMethodCircularDependencyLevelRanges(): Array<LongRange> {
        val countRangeLevel1 = 0L until 5L
        val countRangeLevel2 = 5L until 10L
        val countRangeLevel3 = 10L until Long.MAX_VALUE
        return arrayOf(countRangeLevel1, countRangeLevel2, countRangeLevel3)
    }

    fun getClassCircularDependencyLevelRanges(): Array<LongRange> {
        val countRangeLevel1 = 0L until 10L
        val countRangeLevel2 = 10L until 15L
        val countRangeLevel3 = 15L until Long.MAX_VALUE
        return arrayOf(countRangeLevel1, countRangeLevel2, countRangeLevel3)
    }

    fun getModuleCircularDependencyLevelRanges(): Array<LongRange> {
        val countRangeLevel1 = 0L until 3L
        val countRangeLevel2 = 3L until 5L
        val countRangeLevel3 = 5L until Long.MAX_VALUE
        return arrayOf(countRangeLevel1, countRangeLevel2, countRangeLevel3)
    }

    fun getPackageCircularDependencyLevelRanges(): Array<LongRange> {
        val countRangeLevel1 = 0L until 3L
        val countRangeLevel2 = 3L until 5L
        val countRangeLevel3 = 5L until Long.MAX_VALUE
        return arrayOf(countRangeLevel1, countRangeLevel2, countRangeLevel3)
    }
}
