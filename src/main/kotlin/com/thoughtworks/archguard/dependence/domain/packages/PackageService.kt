package com.thoughtworks.archguard.dependence.domain.packages

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PackageService {
    @Autowired
    lateinit var packageRepository: PackageRepository


    fun getPackageDependence(): PackageGraph {
        val packageStore = PackageStore()
        val results = packageRepository.getPackageDependence()
        results.forEach {
            it.aClz = it.aClz.substringBeforeLast('.')
            it.bClz = it.bClz.substringBeforeLast('.')
        }
        results
                .groupBy { it.aClz }
                .forEach {
                    it.value.groupBy { i -> i.bClz }
                            .forEach { i -> packageStore.addEdge(it.key, i.key, i.value.size) }
                }

        return packageStore.getPackageGraph()
    }
}
