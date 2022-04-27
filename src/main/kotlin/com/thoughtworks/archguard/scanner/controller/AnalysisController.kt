package com.thoughtworks.archguard.scanner.controller

import com.thoughtworks.archguard.scanner.domain.analyser.ArchitectureDependencyAnalysis
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/scanner/systems/{systemId}")
class AnalysisController(@Autowired val dependencyAnalysis: ArchitectureDependencyAnalysis) {
    @PostMapping("/dependency-analyses")
    fun analyseDependency(@PathVariable("systemId") systemId: Long, @RequestParam(defaultValue = "1.6.2") scannerVersion: String,
    ): ResponseEntity<String> {
        return try {
            dependencyAnalysis.asyncAnalyse(systemId, scannerVersion)
            ResponseEntity.ok("")
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @PostMapping("/dependency-analyses/cancel")
    fun cancelAnalyseDependency(@PathVariable("systemId") systemId: Long): ResponseEntity<String> {
        return try {
            dependencyAnalysis.cancelAnalyse(systemId)
            ResponseEntity.ok("")
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }
    }
}
