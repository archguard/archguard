package com.thoughtworks.archguard.metrics.controller

import com.thoughtworks.archguard.metrics.appl.MetricsService
import com.thoughtworks.archguard.metrics.domain.dfms.ClassDfms
import com.thoughtworks.archguard.metrics.domain.dfms.ModuleDfms
import com.thoughtworks.archguard.metrics.domain.dfms.PackageDfms
import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.domain.model.PackageVO
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/systems/{systemId}/metric/dfms")
class DfmsMetricController(val metricsService: MetricsService) {
    @GetMapping("/class")
    fun getClassAbstractMetric(@PathVariable("systemId") systemId: Long,
                               @RequestParam className: String,
                               @RequestParam moduleName: String): ClassDfms {
        return metricsService.getClassDfms(systemId, JClassVO(className, moduleName))
    }

    @GetMapping("/package")
    fun getPackageAbstractMetric(@PathVariable("systemId") systemId: Long,
                                 @RequestParam packageName: String,
                                 @RequestParam moduleName: String): PackageDfms {
        return metricsService.getPackageDfms(systemId, PackageVO(packageName, moduleName))
    }

    @GetMapping("/module")
    fun getModuleAbstractMetric(@PathVariable("systemId") systemId: Long,
                                @RequestParam moduleName: String): ModuleDfms {
        return metricsService.getModuleDfms(systemId, moduleName)
    }
}
