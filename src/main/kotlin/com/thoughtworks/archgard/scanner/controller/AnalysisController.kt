package com.thoughtworks.archgard.scanner.controller

import com.thoughtworks.archgard.scanner.domain.analyser.SqlDependencyAnalysis
import com.thoughtworks.archgard.scanner.domain.analyser.JavaDependencyAnalysis
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/projects/{projectId}")
class AnalysisController(@Autowired val dependencyAnalysis: JavaDependencyAnalysis,
                         @Autowired val sqlAnalysis: SqlDependencyAnalysis) {

    @PostMapping("/dependency-analyses")
    fun analyseDependency(@PathVariable("projectId") id: Long) {
        dependencyAnalysis.analyse(id)
    }

    @PostMapping("/sql-analyses")
    fun analyseSql(@PathVariable("projectId") id: Long) {
        sqlAnalysis.analyse(id)
    }
}
