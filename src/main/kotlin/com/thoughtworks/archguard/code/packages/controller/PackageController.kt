package com.thoughtworks.archguard.code.packages.controller

import com.thoughtworks.archguard.code.packages.domain.ModulePackage
import com.thoughtworks.archguard.code.packages.domain.PackageService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/systems/{systemId}/package")
class PackageController(val packageService: PackageService) {

    @GetMapping("/dependencies")
    fun getPackageDependencies(
        @PathVariable("systemId") systemId: Long,
        @RequestParam(value = "language", required = false, defaultValue = "jvm") language: String,
    ): List<ModulePackage> {
        return packageService.getPackageDependencies(systemId, language)
    }

}
