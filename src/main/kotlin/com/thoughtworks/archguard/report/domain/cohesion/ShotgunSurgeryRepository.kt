package com.thoughtworks.archguard.report.domain.cohesion

interface ShotgunSurgeryRepository {
    fun getShotgunSurgeryCommitIds(systemId: Long, limit: Int): List<String>
    fun getShotgunSurgery(commitIds: List<String>, limit: Long, offset: Long): List<ShotgunSurgery>

}
