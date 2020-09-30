package com.thoughtworks.archguard.report.domain.overview.calculator

import com.thoughtworks.archguard.report.domain.circulardependency.CircularDependencyRepository
import com.thoughtworks.archguard.report.domain.circulardependency.CircularDependencyType
import com.thoughtworks.archguard.report.domain.overview.calculator.base.BaseCalculator
import org.springframework.stereotype.Component

@Component
class CircularDependencyCalculator(val circularDependencyRepository: CircularDependencyRepository) : BaseCalculator {
    override fun getCalculateResult(systemId: Long): BadSmellCalculateResult {
        val methodResult = circularDependencyRepository
                .getCircularDependencyBadSmellCalculateResult(systemId,
                        CircularDependencyType.METHOD,
                        getMethodCircularDependencyLevelRanges())
        val classResult = circularDependencyRepository
                .getCircularDependencyBadSmellCalculateResult(systemId,
                        CircularDependencyType.CLASS,
                        getClassCircularDependencyLevelRanges())
        val packageResult = circularDependencyRepository
                .getCircularDependencyBadSmellCalculateResult(systemId,
                        CircularDependencyType.PACKAGE,
                        getPackageCircularDependencyLevelRanges())

        val moduleResult = circularDependencyRepository
                .getCircularDependencyBadSmellCalculateResult(systemId,
                        CircularDependencyType.MODULE,
                        getModuleCircularDependencyLevelRanges())

        var result = methodResult.plus(classResult)
        result = result.plus(packageResult)
        result = result.plus(moduleResult)
        return result
    }

    private fun getMethodCircularDependencyLevelRanges(): Array<LongRange> {
        val countRangeLevel1 = 0L until 5L
        val countRangeLevel2 = 5L until 10L
        val countRangeLevel3 = 10L until Long.MAX_VALUE
        return arrayOf(countRangeLevel1, countRangeLevel2, countRangeLevel3)
    }

    private fun getClassCircularDependencyLevelRanges(): Array<LongRange> {
        val countRangeLevel1 = 0L until 10L
        val countRangeLevel2 = 10L until 15L
        val countRangeLevel3 = 15L until Long.MAX_VALUE
        return arrayOf(countRangeLevel1, countRangeLevel2, countRangeLevel3)
    }

    private fun getModuleCircularDependencyLevelRanges(): Array<LongRange> {
        val countRangeLevel1 = 0L until 3L
        val countRangeLevel2 = 3L until 5L
        val countRangeLevel3 = 5L until Long.MAX_VALUE
        return arrayOf(countRangeLevel1, countRangeLevel2, countRangeLevel3)
    }

    private fun getPackageCircularDependencyLevelRanges(): Array<LongRange> {
        val countRangeLevel1 = 0L until 3L
        val countRangeLevel2 = 3L until 5L
        val countRangeLevel3 = 5L until Long.MAX_VALUE
        return arrayOf(countRangeLevel1, countRangeLevel2, countRangeLevel3)
    }
}
