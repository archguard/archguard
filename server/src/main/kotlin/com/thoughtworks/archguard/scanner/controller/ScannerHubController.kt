package com.thoughtworks.archguard.scanner.controller

import com.thoughtworks.archguard.scanner.domain.hubexecutor.HubExecutorService
import com.thoughtworks.archguard.scanner.infrastructure.command.InMemoryConsumer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/scanner")
class ScannerHubController(
    @Value("\${spring.datasource.url}") val dbUrl: String,
    @Value("\${spring.datasource.username}") val username: String,
    @Value("\${spring.datasource.password}") val password: String,
    @Autowired var hubService: HubExecutorService
) {


    val url = dbUrl.replace("://", "://$username:$password@")

    @PostMapping("/{id}/reports")
    fun scanModule(@PathVariable("id") id: Long, @RequestParam(defaultValue = "1.6.2") scannerVersion: String): ModuleScanResponse {
        val memoryConsumer = InMemoryConsumer()

        return ModuleScanResponse(
            hubService.doScanIfNotRunning(
                id,
                url,
                scannerVersion,
                memoryConsumer
            )
        )
    }

    @PostMapping("/{id}/evaluations")
    fun evaluate(@PathVariable("id") id: Long, @RequestBody evaluation: EvaluationRequest): ModuleScanResponse {
        return ModuleScanResponse(
            hubService.evaluate(
                evaluation.type,
                id,
                url,
                evaluation.scannerVersion,
                listOf()
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

    data class EvaluationRequest(val type: String, val scannerVersion: String = "1.6.2")
}
