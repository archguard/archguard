package com.thoughtworks.archguard.evaluation.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/evaluations")
class EvaluationReportController() {


    @GetMapping("/{id}")
    fun getEvaluation(@PathVariable id: String) {

    }

    @GetMapping
    fun getEvaluations() {

    }

}