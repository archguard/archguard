package com.thoughtworks.archguard.dependence_package.domain.service

import com.thoughtworks.archguard.dependence_package.domain.dto.PackageGraph
import com.thoughtworks.archguard.dependence_package.domain.repository.PackageRepository
import com.thoughtworks.archguard.dependence_package.domain.store.PackageStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PackageService {
    @Autowired
    lateinit var packageRepository: PackageRepository


    fun getPackageDependence(): PackageGraph {
        var packageStore = PackageStore()
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
