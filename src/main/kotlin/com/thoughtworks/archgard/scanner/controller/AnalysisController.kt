package com.thoughtworks.archgard.scanner.controller

import com.thoughtworks.archgard.scanner.domain.analyser.SqlDependencyAnalysis
import com.thoughtworks.archgard.scanner.domain.analyser.JavaDependencyAnalysis
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class AnalysisController(@Autowired val dependencyAnalysis: JavaDependencyAnalysis,
                         @Autowired val sqlAnalysis: SqlDependencyAnalysis) {

    @PostMapping("/{id}/dependency-analyses")
    fun analyseDependency(@PathVariable("id") id: Long) {
        dependencyAnalysis.analyse(id)
    }

    @PostMapping("/{id}/sql-analyses")
    fun analyseSql(@PathVariable("id") id: Long) {
        sqlAnalysis.analyse(id)
    }
}