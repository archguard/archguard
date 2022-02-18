package com.thoughtworks.archguard.evaluation_bak.domain

import com.thoughtworks.archguard.evaluation_bak.infrastructure.EvaluationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class EvaluationService(@Autowired val evaluationRepository: EvaluationRepository) {
    fun getAll(): List<EvaluationReport> {
        return evaluationRepository.findAll()
    }

    fun getById(id: String): EvaluationReport? {
        return evaluationRepository.findById(id)
    }

    fun getDetailById(id: String): EvaluationReportDetail? {
        return evaluationRepository.findDetailById(id)
    }
}