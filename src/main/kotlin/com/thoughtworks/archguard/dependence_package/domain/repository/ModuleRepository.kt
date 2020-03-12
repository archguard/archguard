package com.thoughtworks.archguard.dependence_package.domain.repository

import com.thoughtworks.archguard.dependence_package.domain.dto.ModuleDefineDTO
import com.thoughtworks.archguard.dependence_package.domain.dto.ModuleDependenceDTO


interface ModuleRepository {
    fun getModuleDependence(packages: List<String>): List<ModuleDependenceDTO>

    fun getModulePackages(): List<ModuleDefineDTO>
}
