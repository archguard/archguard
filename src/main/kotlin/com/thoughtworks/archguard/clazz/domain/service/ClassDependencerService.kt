package com.thoughtworks.archguard.clazz.domain.service

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.config.domain.ConfigureService
import org.springframework.stereotype.Service

@Service
class ClassDependencerService(val repo: JClassRepository, val configureService: ConfigureService) {

    fun findDependencers(projectId: Long, target: JClass, deep: Int): JClass {
        buildDependencers(projectId, listOf(target), deep)
        return target
    }

    private fun buildDependencers(projectId: Long, target: List<JClass>, deep: Int): List<JClass> {
        val container = ArrayList<JClass>()
        doBuildDependencers(projectId, target, deep, container)
        return target
    }

    private fun doBuildDependencers(projectId: Long, target: List<JClass>, deep: Int, container: MutableList<JClass>) {
        var pendingClasses = target.filterNot { container.contains(it) }
        if (pendingClasses.isEmpty() || deep == 0) {
            container.addAll(pendingClasses)
        } else {
            pendingClasses.forEach { it.dependencers = repo.findDependencers(it.id).filter { configureService.isDisplayNode(projectId, it.name) } }
            container.addAll(pendingClasses)
            doBuildDependencers(projectId, pendingClasses.flatMap { it.dependencers }, deep - 1, container)
        }
    }

}
