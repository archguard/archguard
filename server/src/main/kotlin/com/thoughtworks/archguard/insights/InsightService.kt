package com.thoughtworks.archguard.insights

import org.springframework.stereotype.Service

@Service
class InsightService(val repository: InsightRepository) {
    fun byScaArtifact(id: Long, artifact: String): Long {
        return repository.filterByCondition(id, artifact)
    }
}
