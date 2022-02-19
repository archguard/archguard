package com.thoughtworks.archguard.scanner.controller

import com.thoughtworks.archguard.scanner.domain.hubexecutor.HubService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/")
class HubController(@Value("\${spring.datasource.url}") val dbUrl: String,
                    @Value("\${spring.datasource.username}") val username: String,
                    @Value("\${spring.datasource.password}") val password: String) {

    @Autowired
    private lateinit var hubService: HubService

    val url = dbUrl.replace("://", "://$username:$password@")

    @PostMapping("/{id}/reports")
    fun scanModule(@PathVariable("id") id: Long): com.thoughtworks.archguard.scanner.controller.HubController.ModuleScanResponse {
        return com.thoughtworks.archguard.scanner.controller.HubController.ModuleScanResponse(
            hubService.doScanIfNotRunning(
                id,
                url
            )
        )
    }

    @PostMapping("/{id}/evaluations")
    fun evaluate(@PathVariable("id") id: Long, @RequestBody evaluation: com.thoughtworks.archguard.scanner.controller.HubController.EvaluationRequest): com.thoughtworks.archguard.scanner.controller.HubController.ModuleScanResponse {
        return com.thoughtworks.archguard.scanner.controller.HubController.ModuleScanResponse(
            hubService.evaluate(
                evaluation.type,
                id,
                url
            )
        )
    }

    @GetMapping("/{id}/evaluations/status")
    fun evaluate(@PathVariable("id") id: Long, @RequestParam type: String): com.thoughtworks.archguard.scanner.controller.HubController.ModuleScanResponse {
        return com.thoughtworks.archguard.scanner.controller.HubController.ModuleScanResponse(
            hubService.getEvaluationStatus(
                type,
                id
            )
        )
    }

    data class ModuleScanResponse(val isRunning: Boolean)

    data class EvaluationRequest(val type: String)
}