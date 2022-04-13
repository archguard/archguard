package com.thoughtworks.archguard.code.packages.domain

import com.thoughtworks.archguard.code.module.domain.LogicModuleRepository
import com.thoughtworks.archguard.code.packages.infrastructure.PackageDependenceDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PackageService {
    @Autowired
    lateinit var packageRepository: PackageRepository

    @Autowired
    lateinit var moduleRepository: LogicModuleRepository

    fun getPackageDependencies(systemId: Long, language: String): List<ModulePackage> {
        return moduleRepository.getAllSubModule(systemId).map { it.name }.map {
            when(language.lowercase()) {
                "kotlin", "java", "typescript", "csharp", "c#" -> {
                    val dependencies = packageRepository.getPackageDependenceByClass(systemId, it)
                    ModulePackage(it, getPackageGraph(dependencies))
                }
                // when jvm
                else -> {
                    val dependencies = packageRepository.getPackageDependenceByModuleFull(systemId, it)
                    ModulePackage(it, getPackageGraph(dependencies))
                }
            }
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
