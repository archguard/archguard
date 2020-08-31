package com.thoughtworks.archguard.clazz.domain.service

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.config.domain.ConfigureService
import org.springframework.stereotype.Service

@Service
class ClassDependenceesService(val repo: JClassRepository, val configureService: ConfigureService) {
    fun findDependencees(projectId: Long, target: JClass, deep: Int): JClass {
        buildDependencees(projectId, listOf(target), deep)
        return target
    }

    private fun buildDependencees(projectId: Long, target: List<JClass>, deep: Int): List<JClass> {
        val container = ArrayList<JClass>()
        doBuildDependencees(projectId, target, deep, container)
        return target
    }

    private fun doBuildDependencees(projectId: Long, target: List<JClass>, deep: Int, container: MutableList<JClass>) {
        var pendingClasses = target.filterNot { container.contains(it) }
        if (pendingClasses.isEmpty() || deep == 0) {
            container.addAll(pendingClasses)
        } else {
            pendingClasses.forEach { it.dependencees = repo.findDependencees(it.id).filter { configureService.isDisplayNode(projectId, it.name) } }
            container.addAll(pendingClasses)
            doBuildDependencees(projectId, pendingClasses.flatMap { it.dependencees }, deep - 1, container)
        }
    }

}
