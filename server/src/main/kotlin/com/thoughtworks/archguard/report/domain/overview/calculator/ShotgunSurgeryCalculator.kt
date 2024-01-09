package com.thoughtworks.archguard.report.domain.overview.calculator

import com.thoughtworks.archguard.report.domain.cohesion.ShotgunSurgeryService
import org.archguard.smell.BadSmellResult
import org.archguard.smell.BadSmellLevelCalculator
import org.springframework.stereotype.Component

@Component
class ShotgunSurgeryCalculator(val shotgunSurgeryService: ShotgunSurgeryService) : BadSmellLevelCalculator {

    override fun getCalculateResult(systemId: Long): BadSmellResult {
        val shotgunSurgeryCount = shotgunSurgeryService.getShotgunSurgeryCount(systemId)
        return getBadSmellLevel(shotgunSurgeryCount, getLevelRanges())
    }

    private fun getBadSmellLevel(count: Long, range: Array<LongRange>): BadSmellResult {
        return when (count) {
            in range[0] -> BadSmellResult(1L, 0L, 0L)
            in range[1] -> BadSmellResult(0L, 1L, 0L)
            in range[2] -> BadSmellResult(0L, 0L, 1L)
            else -> {
                BadSmellResult(0L, 0L, 0L)
            }
        }
    }

    private fun getLevelRanges(): Array<LongRange> {
        val countRangeLevel1 = 10L until 30L
        val countRangeLevel2 = 30L until 100L
        val countRangeLevel3 = 100L until Long.MAX_VALUE
        return arrayOf(countRangeLevel1, countRangeLevel2, countRangeLevel3)
    }
}
