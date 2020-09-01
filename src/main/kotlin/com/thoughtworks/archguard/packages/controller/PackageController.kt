package com.thoughtworks.archguard.packages.controller

import com.thoughtworks.archguard.packages.domain.ModulePackage
import com.thoughtworks.archguard.packages.domain.PackageService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/systems/{systemId}/package")
class PackageController(val packageService: PackageService) {

    @GetMapping("dependencies")
    fun getPackageDependencies(@PathVariable("systemId") systemId: Long): List<ModulePackage> {
        return packageService.getPackageDependencies(systemId)
    }

}
