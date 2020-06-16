package com.thoughtworks.archguard.dependence.controller.packages

import com.thoughtworks.archguard.dependence.domain.packages.ModulePackage
import com.thoughtworks.archguard.dependence.domain.packages.PackageGraph
import com.thoughtworks.archguard.dependence.domain.packages.PackageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/package")
class PackageController {

    @Autowired
    private lateinit var packageService: PackageService

    @GetMapping("dependencies")
    fun getPackageDependencies(): List<ModulePackage> {
        return packageService.getPackageDependencies()
    }

}