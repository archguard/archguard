package com.thoughtworks.archgard.scanner.controller

import com.thoughtworks.archgard.scanner.domain.hubexecutor.HubService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/")
class HubController {

    @Autowired
    private lateinit var hubService: HubService

    @PostMapping("/{id}/reports")
    fun scanModule(@PathVariable("id") id: Long): ModuleScanResponse {
        return ModuleScanResponse(hubService.doScanIfNotRunning(id))
    }

    @PostMapping("/{id}/evaluations")
    fun evaluate(@PathVariable("id") id: Long, @RequestBody evaluation: EvaluationRequest): ModuleScanResponse {
        return ModuleScanResponse(hubService.evaluate(evaluation.type, id))
    }

    @GetMapping("/{id}/evaluations/status")
    fun evaluate(@PathVariable("id") id: Long, @RequestParam type: String): ModuleScanResponse {
        return ModuleScanResponse(hubService.getEvaluationStatus(type, id))
    }

    data class ModuleScanResponse(val isRunning: Boolean)

    data class EvaluationRequest(val type: String)
}