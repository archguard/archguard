package com.thoughtworks.archguard.evaluation_bak.controller

import com.thoughtworks.archguard.evaluation_bak.domain.EvaluationReport
import com.thoughtworks.archguard.evaluation_bak.domain.EvaluationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/evaluations", produces = ["application/json;charset=utf8"])
class EvaluationReportController(@Autowired val evaluationService: EvaluationService) {

    @GetMapping("/{id}")
    fun getEvaluation(@PathVariable id: String): EvaluationReport? {
        return evaluationService.getById(id)
    }

    @GetMapping
    fun getEvaluations(): List<EvaluationReport> {
        return evaluationService.getAll()
    }

}