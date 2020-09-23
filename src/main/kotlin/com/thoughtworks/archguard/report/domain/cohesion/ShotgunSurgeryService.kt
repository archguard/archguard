package com.thoughtworks.archguard.report.domain.cohesion

import com.thoughtworks.archguard.report.domain.ValidPagingParam
import org.springframework.stereotype.Service

@Service
class ShotgunSurgeryService(val shotgunSurgeryRepository: ShotgunSurgeryRepository) {

    fun getShotgunSurgeryWithTotalCount(systemId: Long, limit: Long, offset: Long): Pair<Long, List<ShotgunSurgery>> {
        ValidPagingParam.validPagingParam(limit, offset)
        val shotgunSurgeryCommitIds = shotgunSurgeryRepository.getShotgunSurgeryCommitIds(systemId, ShotgunSurgery.LIMIT)
        val shotgunSurgeryList = shotgunSurgeryRepository.getShotgunSurgery(shotgunSurgeryCommitIds, limit, offset)
        return (shotgunSurgeryCommitIds.size.toLong() to shotgunSurgeryList)
    }
}
