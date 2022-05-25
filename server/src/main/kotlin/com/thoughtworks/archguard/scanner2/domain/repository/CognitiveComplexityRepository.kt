package com.thoughtworks.archguard.scanner2.domain.repository

import com.thoughtworks.archguard.scanner2.domain.model.CognitiveComplexity

interface CognitiveComplexityRepository {
    fun saveAll(systemId: Long, cognitiveComplexityList: List<CognitiveComplexity>)
}
