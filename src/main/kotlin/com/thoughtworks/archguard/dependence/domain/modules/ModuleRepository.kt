package com.thoughtworks.archguard.dependence.domain.modules

import com.thoughtworks.archguard.dependence.infrastructure.modules.ModuleDefineDTO
import com.thoughtworks.archguard.dependence.infrastructure.modules.ModuleDependenceDTO


interface ModuleRepository {
    fun getModuleDependence(packages: List<String>): List<ModuleDependenceDTO>

    fun getModulePackages(): List<ModuleDefineDTO>
}
