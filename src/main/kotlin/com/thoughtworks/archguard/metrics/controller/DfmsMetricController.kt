package com.thoughtworks.archguard.metrics.controller

import com.thoughtworks.archguard.metrics.domain.MetricsService
import com.thoughtworks.archguard.metrics.domain.dfms.ClassDfms
import com.thoughtworks.archguard.metrics.domain.dfms.ModuleDfms
import com.thoughtworks.archguard.metrics.domain.dfms.PackageDfms
import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.domain.model.PackageVO
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/{projectId}/metric/dfms")
class DfmsMetricController(val metricsService: MetricsService) {
    @GetMapping("/class")
    fun getClassAbstractMetric(@PathVariable("projectId") projectId: Long,
                               @RequestParam className: String,
                               @RequestParam moduleName: String): ClassDfms {
        return metricsService.getClassDfms(projectId, JClassVO(className, moduleName))
    }

    @GetMapping("/package")
    fun getPackageAbstractMetric(@PathVariable("projectId") projectId: Long,
                                 @RequestParam packageName: String,
                                 @RequestParam moduleName: String): PackageDfms {
        return metricsService.getPackageDfms(projectId, PackageVO(packageName, moduleName))
    }

    @GetMapping("/module")
    fun getModuleAbstractMetric(@PathVariable("projectId") projectId: Long,
                                @RequestParam moduleName: String): ModuleDfms {
        return metricsService.getModuleDfms(projectId, moduleName)
    }
}
