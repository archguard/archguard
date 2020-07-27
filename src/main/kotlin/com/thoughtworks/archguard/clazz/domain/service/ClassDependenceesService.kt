package com.thoughtworks.archguard.clazz.domain.service

import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.clazz.domain.JClass
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ClassDependenceesService {
    @Autowired
    private lateinit var repo: JClassRepository
    fun findDependencees(target: JClass, deep: Int): JClass {
        buildDependencees(listOf(target), deep)
        return target
    }

    private fun buildDependencees(target: List<JClass>, deep: Int): List<JClass> {
        val container = ArrayList<JClass>()
        doBuildDependencees(target, deep, container)
        return target
    }

    private fun doBuildDependencees(target: List<JClass>, deep: Int, container: MutableList<JClass>) {
        var pendingClasses = target.filterNot { container.contains(it) }
        if (pendingClasses.isEmpty() || deep == 0) {
            container.addAll(pendingClasses)
        } else {
            pendingClasses.forEach { it.dependencees = repo.findDependencees(it.id) }
            container.addAll(pendingClasses)
            doBuildDependencees(pendingClasses.flatMap { it.dependencees }, deep - 1, container)
        }
    }

}
