package com.thoughtworks.archguard.coverage

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/coverage")
class CoverageController(@Autowired val coverageAnalyzer: CoverageAnalyzer) {

    @GetMapping("/bundle")
    fun bundleCoverage(): Coverage {
        return coverageAnalyzer.analyzeExecFile()
    }
}




