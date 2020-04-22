package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.report.domain.model.Bundle
import com.thoughtworks.archguard.report.infrastructure.CoverageRepo
import com.thoughtworks.archguard.report.domain.model.Dimension
import com.thoughtworks.archguard.report.domain.model.TopItem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/coverage")
class CoverageController(@Autowired val coverageRepo: CoverageRepo) {

    /**获得整个项目的覆盖率信息，数据包含六个维度，按 covered/missed 分成12项*/
    @GetMapping("/bundle")
    fun bundleCoverage(): Bundle {
        return coverageRepo.analyzeExecFile()
    }


    /**
     * 获得覆盖率在指定区间的文件个数
     * @param dms - dimension of coverage, such as instruction,line,branch,complexity,class,method
     */
    @GetMapping("/rateBetween")
    fun countRateBetween(@RequestParam(name = "dms", defaultValue = "line") dms: String,
                         @RequestParam(name = "left", defaultValue = "0") left: Float,
                         @RequestParam(name = "right", defaultValue = "2") right: Float): Int {

        return coverageRepo.countRateBetween(Dimension.valueOf(dms.toUpperCase()), left, right)
    }


    /**
     *
     * @param dms - dimension of coverage, such as instruction,line,branch,complexity,class,method
     */
    @GetMapping("/top")
    fun topN(@RequestParam(name = "dms", defaultValue = "line") dms: String,
             @RequestParam(name = "n", defaultValue = "5") n: Int
    ): List<TopItem> {
        val dmsType = Dimension.valueOf(dms.toUpperCase())
        return coverageRepo.top(dmsType, n)
    }
}






