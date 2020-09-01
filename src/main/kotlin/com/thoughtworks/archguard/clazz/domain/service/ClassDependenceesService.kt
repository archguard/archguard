package com.thoughtworks.archguard.clazz.domain.service

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.config.domain.ConfigureService
import org.springframework.stereotype.Service

@Service
class ClassDependenceesService(val repo: JClassRepository, val configureService: ConfigureService, val classConfigService: ClassConfigService) {
    fun findDependencees(systemId: Long, target: JClass, deep: Int): JClass {
        buildDependencees(systemId, listOf(target), deep)
        return target
    }

    private fun buildDependencees(systemId: Long, target: List<JClass>, deep: Int): List<JClass> {
        val container = ArrayList<JClass>()
        doBuildDependencees(systemId, target, deep, container)
        return target
    }

    private fun doBuildDependencees(systemId: Long, target: List<JClass>, deep: Int, container: MutableList<JClass>) {
        val pendingClasses = target.filterNot { container.contains(it) }
        if (pendingClasses.isEmpty() || deep == 0) {
            container.addAll(pendingClasses)
        } else {
            pendingClasses.forEach {
                val dependencees = repo.findDependencees(it.id).filter { configureService.isDisplayNode(systemId, it.name) }
                classConfigService.buildJClassColorConfig(dependencees, systemId)
                it.dependencees = dependencees
            }
            container.addAll(pendingClasses)
            doBuildDependencees(systemId, pendingClasses.flatMap { it.dependencees }, deep - 1, container)
        }
    }

}
