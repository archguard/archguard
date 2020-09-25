package com.thoughtworks.archguard.report.domain.overgeneralization

import com.thoughtworks.archguard.report.domain.module.ClassVO
import org.springframework.stereotype.Service

@Service
class OverGeneralizationService(val overGeneralizationRepository: OverGeneralizationRepository) {
    fun getOneExtendsWithTotalCount(systemId: Long, limit: Long, offset: Long): Pair<Long, List<ClassVO>> {
        val count = overGeneralizationRepository.getOverGeneralizationCount(systemId)
        val list = overGeneralizationRepository.getOverGeneralizationList(systemId, limit, offset)
        return (count to list)
    }

}
