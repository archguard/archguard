package com.thoughtworks.archguard.scanner2.domain.repository

import org.archguard.threshold.CognitiveComplexity

interface CognitiveComplexityRepository {
    fun saveAll(systemId: Long, cognitiveComplexityList: List<CognitiveComplexity>)
}
