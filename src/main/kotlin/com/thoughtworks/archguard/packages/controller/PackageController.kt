package com.thoughtworks.archguard.packages.controller

import com.thoughtworks.archguard.packages.domain.ModulePackage
import com.thoughtworks.archguard.packages.domain.PackageService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/systems/{systemId}/package")
class PackageController(val packageService: PackageService) {

    @GetMapping("dependencies")
    fun getPackageDependencies(
        @PathVariable("systemId") systemId: Long,
        @RequestParam(value = "language", required = false, defaultValue = "jvm") language: String,
    ): List<ModulePackage> {
        return packageService.getPackageDependencies(systemId, language)
    }

}
