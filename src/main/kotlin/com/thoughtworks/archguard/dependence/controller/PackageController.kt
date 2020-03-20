package com.thoughtworks.archguard.dependence.controller

import com.thoughtworks.archguard.dependence.domain.packages.PackageGraph
import com.thoughtworks.archguard.dependence.domain.packages.PackageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/")
class PackageController {

    @Autowired
    private lateinit var packageService: PackageService

    @GetMapping("/package/dependence/all")
    fun getPackageDependence(): PackageGraph {
        return packageService.getPackageDependence()
    }

}