package com.thoughtworks.archgard.scanner.controller

import com.thoughtworks.archgard.scanner.domain.analyser.JavaDependencyAnalysis
import com.thoughtworks.archgard.scanner.domain.analyser.SqlDependencyAnalysis
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/systems/{systemId}")
class AnalysisController(@Autowired val dependencyAnalysis: JavaDependencyAnalysis,
                         @Autowired val sqlAnalysis: SqlDependencyAnalysis) {

    @PostMapping("/dependency-analyses")
    fun analyseDependency(@PathVariable("systemId") systemId: Long): ResponseEntity<String> {
        return try {
            dependencyAnalysis.asyncAnalyse(systemId)
            ResponseEntity.ok("")
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @PostMapping("/sql-analyses")
    fun analyseSql(@PathVariable("systemId") id: Long) {
        sqlAnalysis.analyse(id)
    }
}
