package com.thoughtworks.archguard.metrics.controller

import com.thoughtworks.archguard.code.module.domain.model.PackageVO
import com.thoughtworks.archguard.metrics.domain.dfms.ClassDfms
import com.thoughtworks.archguard.metrics.domain.dfms.DfmsApplService
import com.thoughtworks.archguard.metrics.domain.dfms.ModuleDfms
import com.thoughtworks.archguard.metrics.domain.dfms.PackageDfms
import org.archguard.model.vos.JClassVO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/systems/{systemId}/metric/dfms")
class DfmsMetricController(val dfmsApplService: DfmsApplService) {
    @GetMapping("/class")
    fun getClassAbstractMetric(
        @PathVariable("systemId") systemId: Long,
        @RequestParam className: String,
        @RequestParam moduleName: String
    ): ClassDfms {
        return dfmsApplService.getClassDfms(systemId, JClassVO(className, moduleName))
    }

    @GetMapping("/package")
    fun getPackageAbstractMetric(
        @PathVariable("systemId") systemId: Long,
        @RequestParam packageName: String,
        @RequestParam moduleName: String
    ): PackageDfms {
        return dfmsApplService.getPackageDfms(systemId, PackageVO(packageName, moduleName))
    }

    @GetMapping("/module")
    fun getModuleAbstractMetric(
        @PathVariable("systemId") systemId: Long,
        @RequestParam moduleName: String
    ): ModuleDfms {
        return dfmsApplService.getModuleDfms(systemId, moduleName)
    }
}
