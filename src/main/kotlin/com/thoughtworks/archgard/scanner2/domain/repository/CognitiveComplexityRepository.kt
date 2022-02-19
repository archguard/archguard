package com.thoughtworks.archgard.scanner2.domain.repository

import com.thoughtworks.archgard.scanner2.domain.model.CognitiveComplexity

interface CognitiveComplexityRepository {
    fun saveAll(systemId: Long, cognitiveComplexityList: List<CognitiveComplexity>)

}
