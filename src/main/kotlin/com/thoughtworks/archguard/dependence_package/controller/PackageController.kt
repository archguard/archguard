package com.thoughtworks.archguard.dependence_package.controller

import com.thoughtworks.archguard.dependence_package.domain.dto.PackageGraph
import com.thoughtworks.archguard.dependence_package.domain.service.PackageService
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