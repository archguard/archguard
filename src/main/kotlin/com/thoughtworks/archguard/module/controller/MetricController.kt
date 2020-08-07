package com.thoughtworks.archguard.module.controller

import com.thoughtworks.archguard.module.domain.MetricsService
import com.thoughtworks.archguard.module.domain.metrics.abstracts.ClassAbstractRatio
import com.thoughtworks.archguard.module.domain.metrics.abstracts.ModuleAbstractRatio
import com.thoughtworks.archguard.module.domain.metrics.abstracts.PackageAbstractRatio
import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.domain.model.PackageVO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/metric")
class MetricController(val metricsService: MetricsService) {
    @GetMapping("/class")
    fun getClassAbstractMetric(@RequestParam className: String, @RequestParam moduleName: String): ClassAbstractRatio {
        return metricsService.getClassAbstractMetric(JClassVO(className, moduleName))
    }

    @GetMapping("/package")
    fun getPackageAbstractMetric(@RequestParam packageName: String, @RequestParam moduleName: String): PackageAbstractRatio {
        return metricsService.getPackageAbstractMetric(PackageVO(packageName, moduleName))
    }

    @GetMapping("/module")
    fun getModuleAbstractMetric(@RequestParam moduleName: String): ModuleAbstractRatio {
        return metricsService.getModuleAbstractMetric(moduleName)
    }

}