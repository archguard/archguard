package com.thoughtworks.archguard.evaluation_bak.controller

import com.thoughtworks.archguard.evaluation_bak.domain.QualityEvaluationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/quality-evaluations")
class QualityEvaluationController(@Autowired val qualityEvaluationService: QualityEvaluationService) {

    @PostMapping
    fun generateQualityEvaluation(): String {
        return qualityEvaluationService.generateEvaluation()
    }
}