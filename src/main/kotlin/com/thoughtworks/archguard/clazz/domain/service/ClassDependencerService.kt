package com.thoughtworks.archguard.clazz.domain.service

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.config.domain.ConfigureService
import org.springframework.stereotype.Service

@Service
class ClassDependencerService(val repo: JClassRepository, val configureService: ConfigureService, val classConfigService: ClassConfigService) {

    fun findDependencers(systemId: Long, target: JClass, deep: Int): JClass {
        buildDependencers(systemId, listOf(target), deep)
        return target
    }

    private fun buildDependencers(systemId: Long, target: List<JClass>, deep: Int): List<JClass> {
        val container = ArrayList<JClass>()
        doBuildDependencers(systemId, target, deep, container)
        return target
    }

    private fun doBuildDependencers(systemId: Long, target: List<JClass>, deep: Int, container: MutableList<JClass>) {
        val pendingClasses = target.filterNot { container.contains(it) }
        if (pendingClasses.isEmpty() || deep == 0) {
            container.addAll(pendingClasses)
        } else {
            pendingClasses.forEach {
                val dependencers = repo.findDependencers(it.id).filter { configureService.isDisplayNode(systemId, it.name) }
                classConfigService.buildJClassColorConfig(dependencers, systemId)
                it.dependencers = dependencers
            }
            container.addAll(pendingClasses)
            doBuildDependencers(systemId, pendingClasses.flatMap { it.dependencers }, deep - 1, container)
        }
    }

}
