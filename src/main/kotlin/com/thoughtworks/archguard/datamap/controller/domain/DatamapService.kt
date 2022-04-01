package com.thoughtworks.archguard.datamap.controller.domain

import org.springframework.stereotype.Service

@Service
class DatamapService(val repository: DatamapRepository) {
    fun getDatamapBySystemId(systemId: Long): List<Datamap> {
        return repository.getDatamapBySystemId(systemId)
    }
}