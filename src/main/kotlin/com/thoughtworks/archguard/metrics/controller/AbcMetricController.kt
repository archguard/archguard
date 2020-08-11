package com.thoughtworks.archguard.metrics.controller

import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.method.domain.JMethodRepository
import com.thoughtworks.archguard.metrics.domain.abc.AbcService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/metric/abc")
class AbcMetricController(val abcService: AbcService, val jClassRepository: JClassRepository, val methodRepo: JMethodRepository) {
    @GetMapping("/class")
    fun getClassAbcMetric(@RequestParam className: String, @RequestParam moduleName: String): Int {
        val jClass = jClassRepository.getJClassBy(className, moduleName)
                ?: throw ClassNotFoundException("Cannot find class by name: $className module: $moduleName")
        jClass.methods = methodRepo.findMethodsByModuleAndClass(jClass.module, jClass.name)
        return abcService.calculateAbc(jClass)
    }
}