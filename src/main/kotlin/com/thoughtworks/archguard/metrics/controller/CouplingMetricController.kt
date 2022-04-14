package com.thoughtworks.archguard.metrics.controller

import com.thoughtworks.archguard.code.module.domain.LogicModuleRepository
import com.thoughtworks.archguard.code.module.domain.model.JClassVO
import com.thoughtworks.archguard.code.module.domain.model.PackageVO
import com.thoughtworks.archguard.metrics.domain.coupling.ClassCoupling
import com.thoughtworks.archguard.metrics.domain.coupling.CouplingService
import com.thoughtworks.archguard.metrics.domain.coupling.ModuleCoupling
import com.thoughtworks.archguard.metrics.domain.coupling.PackageCoupling
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/systems/{systemId}/metric/coupling")
class CouplingMetricController(val couplingService: CouplingService, val logicModuleRepository: LogicModuleRepository) {
    @GetMapping("/class")
    fun getClassCouplingMetric(
        @PathVariable("systemId") systemId: Long,
        @RequestParam className: String,
        @RequestParam moduleName: String
    ): ClassCoupling {
        return couplingService.calculateClassCoupling(systemId, JClassVO(className, moduleName))
    }

    @GetMapping("/package-class-list")
    fun getPackageClassCouplingMetric(
        @PathVariable("systemId") systemId: Long,
        @RequestParam packageName: String,
        @RequestParam moduleName: String
    ): List<ClassCoupling> {
        return couplingService.calculatePackageDirectClassCouplings(systemId, PackageVO(packageName, moduleName))
    }

    @PostMapping("/package-list")
    fun getPackagesCouplingMetric(
        @PathVariable("systemId") systemId: Long,
        @RequestBody packageNameList: List<String>
    ): List<PackageCoupling> {
        return packageNameList.map { couplingService.calculatePackageCoupling(systemId, PackageVO.fromFullName(it)) }
    }

    @GetMapping("/package")
    fun getPackageCouplingMetric(
        @PathVariable("systemId") systemId: Long,
        @RequestParam packageName: String,
        @RequestParam moduleName: String
    ): PackageCoupling {
        return couplingService.calculatePackageCoupling(systemId, PackageVO(packageName, moduleName))
    }

    @GetMapping("/module")
    fun getModuleCouplingMetric(
        @PathVariable("systemId") systemId: Long,
        @RequestParam moduleName: String
    ): ModuleCoupling {
        return couplingService.calculateModuleCoupling(systemId, logicModuleRepository.get(systemId, moduleName))
    }

    @GetMapping("/all-module")
    fun getAllModuleCouplingMetric(@PathVariable("systemId") systemId: Long): List<ModuleCoupling> {
        return couplingService.calculateAllModuleCoupling(systemId)
    }

    @PostMapping("/persist")
    @ResponseStatus(HttpStatus.OK)
    fun persistCouplingMetric(@PathVariable("systemId") systemId: Long) {
        couplingService.persistAllClassCouplingResults(systemId)
    }
}
