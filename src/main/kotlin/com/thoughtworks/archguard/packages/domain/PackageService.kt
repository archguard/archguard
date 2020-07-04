package com.thoughtworks.archguard.packages.domain

import com.thoughtworks.archguard.module.domain.BaseModuleRepository
import com.thoughtworks.archguard.packages.infrastructure.PackageDependenceDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PackageService {
    @Autowired
    lateinit var packageRepository: PackageRepository

    @Autowired
    lateinit var moduleRepository: BaseModuleRepository

    fun getPackageDependencies(): List<ModulePackage> {
        return moduleRepository.getBaseModules().map {
            val dependencies = packageRepository.getPackageDependenceByModule(it)
            ModulePackage(it, getPackageGraph(dependencies))
        }
    }

    private fun getPackageGraph(results: List<PackageDependenceDTO>): PackageGraph {
        val packageStore = PackageStore()
        results.forEach {
            it.aClz = it.aClz.substringBeforeLast('.')
            it.bClz = it.bClz.substringBeforeLast('.')
        }
        results.filter {
            !it.aClz.contains("$")
                    && !it.aClz.contains("[")
                    && !it.bClz.contains("$")
                    && !it.bClz.contains("[")
        }
                .groupBy { it.aClz }
                .forEach {
                    it.value.groupBy { i -> i.bClz }
                            .forEach { i -> packageStore.addEdge(it.key, i.key, i.value.size) }
                }

        return packageStore.getPackageGraph()
    }
}
