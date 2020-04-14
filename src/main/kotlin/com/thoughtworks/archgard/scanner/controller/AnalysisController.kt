package com.thoughtworks.archgard.scanner.controller

import com.thoughtworks.archgard.scanner.domain.analyser.SqlDependencyAnalysis
import com.thoughtworks.archgard.scanner.domain.analyser.JavaDependencyAnalysis
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class AnalysisController(@Autowired val dependencyAnalysis: JavaDependencyAnalysis,
                         @Autowired val sqlAnalysis: SqlDependencyAnalysis) {

    @PostMapping("/dependency-analyses")
    fun analyseDependency() {
        dependencyAnalysis.analyse();
    }

    @PostMapping("/sql-analyses")
    fun analyseSql() {
        sqlAnalysis.analyse()
    }
}