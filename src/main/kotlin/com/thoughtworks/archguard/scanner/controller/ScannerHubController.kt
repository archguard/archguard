package com.thoughtworks.archguard.scanner.controller

import com.thoughtworks.archguard.scanner.domain.hubexecutor.HubExecutorService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/scanner")
class ScannerHubController(@Value("\${spring.datasource.url}") val dbUrl: String,
                           @Value("\${spring.datasource.username}") val username: String,
                           @Value("\${spring.datasource.password}") val password: String) {

    @Autowired
    private lateinit var hubService: HubExecutorService

    val url = dbUrl.replace("://", "://$username:$password@")

    @PostMapping("/{id}/reports")
    fun scanModule(@PathVariable("id") id: Long): ModuleScanResponse {
        return ModuleScanResponse(
            hubService.doScanIfNotRunning(
                id,
                url
            )
        )
    }

    @PostMapping("/{id}/evaluations")
    fun evaluate(@PathVariable("id") id: Long, @RequestBody evaluation: EvaluationRequest): ModuleScanResponse {
        return ModuleScanResponse(
            hubService.evaluate(
                evaluation.type,
                id,
                url
            )
        )
    }

    @GetMapping("/{id}/evaluations/status")
    fun evaluate(@PathVariable("id") id: Long, @RequestParam type: String): ModuleScanResponse {
        return ModuleScanResponse(
            hubService.getEvaluationStatus(
                type,
                id
            )
        )
    }

    data class ModuleScanResponse(val isRunning: Boolean)

    data class EvaluationRequest(val type: String)
}