package com.thoughtworks.archgard.scanner.controller

import com.thoughtworks.archgard.scanner.domain.scanner.dependencies.JavaDependencyAnalysis
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class AnalysisController(@Autowired val dependencyAnalysis: JavaDependencyAnalysis) {

    @PostMapping("/dependency-analyses")
    fun analyseDependency() {
        dependencyAnalysis.analysis();
    }

    @PostMapping("/sql-analyses")
    fun analyseSql() {
    }
}