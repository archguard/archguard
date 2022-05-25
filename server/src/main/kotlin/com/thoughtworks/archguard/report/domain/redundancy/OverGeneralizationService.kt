package com.thoughtworks.archguard.report.domain.redundancy

import com.thoughtworks.archguard.report.domain.ValidPagingParam
import com.thoughtworks.archguard.report.domain.badsmell.BadSmellType
import org.springframework.stereotype.Service

@Service
class OverGeneralizationService(val overGeneralizationRepository: OverGeneralizationRepository) {

    fun getOneExtendsWithTotalCount(systemId: Long, limit: Long, offset: Long): Pair<Long, List<OverGeneralizationPair>> {
        ValidPagingParam.validPagingParam(limit, offset)
        val parentClasses = overGeneralizationRepository.getOverGeneralizationParentClassId(systemId)
        if (parentClasses.isEmpty()) {
            val emptyList = listOf<OverGeneralizationPair>()
            return (0L to emptyList)
        }

        val pairList = overGeneralizationRepository.getOverGeneralizationPairList(parentClasses, limit, offset)
        return (parentClasses.size.toLong() to pairList)
    }

    fun getRedundantReport(systemId: Long): Map<BadSmellType, Long> {
        val overGeneralizationCount = overGeneralizationRepository.getOverGeneralizationCount(systemId)
        return mapOf((BadSmellType.OVER_GENERALIZATION to overGeneralizationCount))
    }
}
