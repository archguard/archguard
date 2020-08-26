package com.thoughtworks.archguard.metrics.controller

import com.thoughtworks.archguard.metrics.domain.coupling.ClassCoupling
import com.thoughtworks.archguard.metrics.domain.coupling.CouplingService
import com.thoughtworks.archguard.metrics.domain.coupling.ModuleCoupling
import com.thoughtworks.archguard.metrics.domain.coupling.PackageCoupling
import com.thoughtworks.archguard.module.domain.LogicModuleRepository
import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.domain.model.PackageVO
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/projects/{projectId}/metric/coupling")
class CouplingMetricController(val couplingService: CouplingService, val logicModuleRepository: LogicModuleRepository) {
    @GetMapping("/class")
    fun getClassCouplingMetric(@PathVariable("projectId") projectId: Long,
                               @RequestParam className: String,
                               @RequestParam moduleName: String): ClassCoupling {
        return couplingService.calculateClassCoupling(projectId, JClassVO(className, moduleName))
    }

    @GetMapping("/package-class-list")
    fun getPackageClassCouplingMetric(@PathVariable("projectId") projectId: Long,
                                      @RequestParam packageName: String,
                                      @RequestParam moduleName: String): List<ClassCoupling> {
        return couplingService.calculatePackageDirectClassCouplings(projectId, PackageVO(packageName, moduleName))
    }

    @PostMapping("/package-list")
    fun getPackagesCouplingMetric(@PathVariable("projectId") projectId: Long,
                                  @RequestBody packageNameList: List<String>): List<PackageCoupling> {
        return packageNameList.map { couplingService.calculatePackageCoupling(projectId, PackageVO.fromFullName(it)) }
    }

    @GetMapping("/package")
    fun getPackageCouplingMetric(@PathVariable("projectId") projectId: Long,
                                 @RequestParam packageName: String,
                                 @RequestParam moduleName: String): PackageCoupling {
        return couplingService.calculatePackageCoupling(projectId, PackageVO(packageName, moduleName))
    }

    @GetMapping("/module")
    fun getModuleCouplingMetric(@PathVariable("projectId") projectId: Long,
                                @RequestParam moduleName: String): ModuleCoupling {
        return couplingService.calculateModuleCoupling(projectId, logicModuleRepository.get(projectId, moduleName))
    }

    @GetMapping("/all-module")
    fun getAllModuleCouplingMetric(@PathVariable("projectId") projectId: Long): List<ModuleCoupling> {
        return couplingService.calculateAllModuleCoupling(projectId)
    }

    @PostMapping("/persist")
    @ResponseStatus(HttpStatus.OK)
    fun persistCouplingMetric(@PathVariable("projectId") projectId: Long) {
        couplingService.persistAllClassCouplingResults(projectId)
    }
}
