package com.thoughtworks.archguard.report.domain.cohesion

import com.thoughtworks.archguard.report.domain.ValidPagingParam
import com.thoughtworks.archguard.report.domain.badsmell.BadSmellType
import org.springframework.stereotype.Service

@Service
class ShotgunSurgeryService(val shotgunSurgeryRepository: ShotgunSurgeryRepository) {

    fun getShotgunSurgeryWithTotalCount(systemId: Long, limit: Long, offset: Long): Pair<Long, List<ShotgunSurgery>> {
        ValidPagingParam.validPagingParam(limit, offset)
        val shotgunSurgeryCommitIds = shotgunSurgeryRepository.getShotgunSurgeryCommitIds(systemId, ShotgunSurgery.LIMIT)
        val shotgunSurgeryList = shotgunSurgeryRepository.getShotgunSurgery(shotgunSurgeryCommitIds, limit, offset)
        return (shotgunSurgeryCommitIds.size.toLong() to shotgunSurgeryList)
    }

    fun getShotgunSurgeryCount(systemId: Long): Long {
        val shotgunSurgeryCommitIds = shotgunSurgeryRepository.getShotgunSurgeryCommitIds(systemId, ShotgunSurgery.LIMIT)
        return shotgunSurgeryCommitIds.size.toLong()
    }

    fun getCohesionReport(systemId: Long): Map<BadSmellType, Long> {
        val count = this.getShotgunSurgeryCount(systemId)
        return mapOf((BadSmellType.SHOTGUN_SURGERY to count))
    }
}
