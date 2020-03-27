package com.thoughtworks.archguard.coverage

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/coverage")
class CoverageController(@Autowired val coverageAnalyzer: CoverageAnalyzer) {

    @GetMapping("/bundle")
    fun bundleCoverage(): Bundle {
        return coverageAnalyzer.analyzeExecFile()
    }


    @GetMapping("/rateBetween")
    fun countRateBetween(@RequestParam(name = "dms", defaultValue = "line") dms: String,
                         @RequestParam(name = "left", defaultValue = "0") left: Float,
                         @RequestParam(name = "right", defaultValue = "2") right: Float): Int {

        return coverageAnalyzer.countRateBetween(Dimension.valueOf(dms.toUpperCase()), left, right)
    }


    @GetMapping("/top")
    fun topN(@RequestParam(name = "dms", defaultValue = "line") dms: String,
             @RequestParam(name = "n", defaultValue = "5") n: Int
    ): List<TopItem> {
        val dmsType = Dimension.valueOf(dms.toUpperCase())
        return coverageAnalyzer.top(dmsType, n)
    }
}






