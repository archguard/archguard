package com.thoughtworks.archguard.clazz.domain.service

import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.clazz.domain.JClass
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ClassDependencerService {
    @Autowired
    private lateinit var repo: JClassRepository

    fun findDependencers(target: List<JClass>, deep: Int): List<JClass> {
        return buildDependencers(target, deep)
    }

    private fun buildDependencers(target: List<JClass>, deep: Int): List<JClass> {
        val container = ArrayList<JClass>()
        doBuildDependencers(target, deep, container)
        return container[0].dependencers
    }

    private fun doBuildDependencers(target: List<JClass>, deep: Int, container: MutableList<JClass>) {
        var pendingClasses = target.filterNot { container.contains(it) }
        if (pendingClasses.isEmpty() || deep == 0) {
            container.addAll(pendingClasses)
        } else {
            pendingClasses.forEach { it.dependencers = findDependencers(it) }
            container.addAll(pendingClasses)
            doBuildDependencers(pendingClasses.flatMap { it.dependencers }, deep - 1, container)
        }
    }

    private fun findDependencers(clazz: JClass): List<JClass> {
        return repo.findDependencers(clazz.id)
    }

}
