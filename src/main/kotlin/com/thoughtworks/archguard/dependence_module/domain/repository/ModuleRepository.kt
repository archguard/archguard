package com.thoughtworks.archguard.dependence_module.domain.repository

import com.thoughtworks.archguard.dependence_module.domain.dto.ModuleDefineDTO
import com.thoughtworks.archguard.dependence_module.domain.dto.ModuleDependenceDTO


interface ModuleRepository {
    fun getModuleDependence(packages: List<String>): List<ModuleDependenceDTO>

    fun getModulePackages(): List<ModuleDefineDTO>
}
