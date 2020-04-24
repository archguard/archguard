package com.thoughtworks.archguard.evaluation.domain

import com.thoughtworks.archguard.evaluation.infrastructure.EvaluationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EvaluationService(@Autowired val evaluationRepository: EvaluationRepository) {
    fun getAll(): List<EvaluationReport> {
        return evaluationRepository.findAll()
    }
}