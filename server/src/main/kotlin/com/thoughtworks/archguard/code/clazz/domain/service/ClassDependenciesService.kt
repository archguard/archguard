package com.thoughtworks.archguard.code.clazz.domain.service

import com.thoughtworks.archguard.code.clazz.domain.JClass
import com.thoughtworks.archguard.code.clazz.domain.JClassRepository
import com.thoughtworks.archguard.config.domain.ConfigureService
import org.springframework.stereotype.Service

@Service
class ClassDependenciesService(val repo: JClassRepository, val configureService: ConfigureService, val classConfigService: ClassConfigService) {
    fun findDependencies(systemId: Long, target: JClass, deep: Int): JClass {
        buildDependencies(systemId, listOf(target), deep)
        return target
    }

    private fun buildDependencies(systemId: Long, target: List<JClass>, deep: Int): List<JClass> {
        val container = ArrayList<JClass>()
        doBuildDependencies(systemId, target, deep, container)
        return target
    }

    private fun doBuildDependencies(systemId: Long, target: List<JClass>, deep: Int, container: MutableList<JClass>) {
        val pendingClasses = target.filterNot { container.contains(it) }
        if (pendingClasses.isEmpty() || deep == 0) {
            container.addAll(pendingClasses)
        } else {
            pendingClasses.forEach {
                val dependencies = repo.findDependencees(it.id).filter { configureService.isDisplayNode(systemId, it.name) }
                classConfigService.buildJClassColorConfig(dependencies, systemId)
                it.dependencies = dependencies
            }
            container.addAll(pendingClasses)
            doBuildDependencies(systemId, pendingClasses.flatMap { it.dependencies }, deep - 1, container)
        }
    }
}
