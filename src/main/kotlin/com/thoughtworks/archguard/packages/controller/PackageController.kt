package com.thoughtworks.archguard.packages.controller

import com.thoughtworks.archguard.packages.domain.ModulePackage
import com.thoughtworks.archguard.packages.domain.PackageService
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