package com.thoughtworks.archguard.v2.frontier.clazz.domain.service

import com.thoughtworks.archguard.config.domain.ConfigureService
import com.thoughtworks.archguard.v2.frontier.clazz.domain.JClass
import com.thoughtworks.archguard.v2.frontier.clazz.domain.JClassRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ClassDependencerService(
    @Autowired private val repo: JClassRepository,
    @Autowired private val configureService: ConfigureService,
    @Autowired private val classConfigService: ClassConfigService,
) {
    fun findDependencers(systemId: Long, target: JClass, deep: Int): JClass {
        buildDependencers(systemId, listOf(target), deep)
        return target
    }

    private fun buildDependencers(systemId: Long, target: List<JClass>, deep: Int): List<JClass> {
        doBuildDependencers(systemId, target.toSet(), deep, mutableSetOf())
        return target
    }

    private fun doBuildDependencers(
        systemId: Long,
        target: Set<JClass>,
        deep: Int,
        processedClasses: MutableSet<JClass>,
    ) {
        val remainingClasses = target.subtract(processedClasses)
        if (remainingClasses.isEmpty() || deep == 0) {
            processedClasses.addAll(remainingClasses)
            return
        }
        for (clazz in remainingClasses) {
            clazz.dependencers =
                repo.findDependencers(clazz.id).filter { configureService.isDisplayNode(systemId, it.name) }
                    .also { classConfigService.buildJClassColorConfig(it, systemId) }
        }
        processedClasses.addAll(remainingClasses)

        doBuildDependencers(
            systemId, remainingClasses.flatMap { it.dependencers }.toSet(), deep - 1, processedClasses
        )
    }
}
