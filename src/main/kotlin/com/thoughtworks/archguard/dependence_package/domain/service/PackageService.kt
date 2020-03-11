package com.thoughtworks.archguard.dependence_package.domain.service

import com.thoughtworks.archguard.dependence_package.domain.dto.PackageGraph
import com.thoughtworks.archguard.dependence_package.domain.repository.PackageRepository
import com.thoughtworks.archguard.dependence_package.domain.util.PackageStore
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
        results.groupBy { it.aClz }
                .mapValues { it.value.groupBy { i -> i.bClz }.mapValues { i -> i.value.size } }
                .mapValues { it.value.mapKeys { i -> Pair(it.key, i.key) } }
                .forEach { it.value.forEach { i -> packageStore.addEdge(i.key.first, i.key.second, i.value) } }

        return packageStore.getPackageGraph()
    }
}
