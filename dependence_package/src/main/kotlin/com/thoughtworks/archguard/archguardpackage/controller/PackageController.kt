package com.thoughtworks.archguard.archguardpackage.controller

import com.thoughtworks.archguard.archguardpackage.domain.dto.PackageGraph
import com.thoughtworks.archguard.archguardpackage.domain.service.PackageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/")
class PackageController {

    @Autowired
    private lateinit var packageService: PackageService

    @GetMapping("/dependency/package")
    fun getPackageDependence(): PackageGraph {
        return packageService.getPackageDependence()
    }

}