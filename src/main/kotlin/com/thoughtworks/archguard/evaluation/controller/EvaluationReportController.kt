package com.thoughtworks.archguard.evaluation.controller

import com.thoughtworks.archguard.evaluation.domain.EvaluationReport
import com.thoughtworks.archguard.evaluation.domain.EvaluationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/evaluations")
class EvaluationReportController(@Autowired val evaluationService: EvaluationService) {


    @GetMapping("/{id}")
    fun getEvaluation(@PathVariable id: String) {

    }

    @GetMapping(produces = ["application/json;charset=utf8"])
    fun getEvaluations(): List<EvaluationReport> {
       return evaluationService.getAll()
    }

}