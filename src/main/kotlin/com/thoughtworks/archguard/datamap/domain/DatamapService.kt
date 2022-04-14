package com.thoughtworks.archguard.datamap.domain

import org.springframework.stereotype.Service

@Service
class DatamapService(val repository: DatamapRepository) {
    fun getDatamapBySystemId(systemId: Long): List<Datamap> {
        return repository.getDatamapBySystemId(systemId)
    }
}
