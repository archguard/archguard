package com.thoughtworks.archguard.evaluation.controller

import com.thoughtworks.archguard.evaluation.domain.EvaluationReportDetail
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/evaluation-details")
class ScanReportController {

    @GetMapping("/{id}")
    fun getEvaluation(@PathVariable id: String): EvaluationReportDetail? {
        return null
    }
}