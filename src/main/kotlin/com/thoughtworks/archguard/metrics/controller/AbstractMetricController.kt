package com.thoughtworks.archguard.metrics.controller

import com.thoughtworks.archguard.metrics.domain.MetricsService
import com.thoughtworks.archguard.metrics.domain.abstracts.ClassAbstractRatio
import com.thoughtworks.archguard.metrics.domain.abstracts.ModuleAbstractRatio
import com.thoughtworks.archguard.metrics.domain.abstracts.PackageAbstractRatio
import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.domain.model.PackageVO
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/{projectId}/metric/abstract")
class AbstractMetricController(val metricsService: MetricsService) {
    @GetMapping("/class")
    fun getClassAbstractMetric(@PathVariable("projectId") projectId: Long,
                               @RequestParam className: String,
                               @RequestParam moduleName: String): ClassAbstractRatio {
        return metricsService.getClassAbstractMetric(JClassVO(className, moduleName))
    }

    @GetMapping("/package")
    fun getPackageAbstractMetric(@PathVariable("projectId") projectId: Long,
                                 @RequestParam packageName: String,
                                 @RequestParam moduleName: String): PackageAbstractRatio {
        return metricsService.getPackageAbstractMetric(projectId, PackageVO(packageName, moduleName))
    }

    @GetMapping("/module")
    fun getModuleAbstractMetric(@PathVariable("projectId") projectId: Long,
                                @RequestParam moduleName: String): ModuleAbstractRatio {
        return metricsService.getModuleAbstractMetric(projectId, moduleName)
    }

}
