package com.thoughtworks.archguard.clazz.domain.service

import com.thoughtworks.archguard.clazz.exception.ClassNoIdException
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.module.domain.model.JClass
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ClassDependenceesService {
    @Autowired
    private lateinit var repo: JClassRepository
    fun findDependencees(name: String, deep: Int): List<JClass> {
        val target = repo.getJClassByName(name)
        return buildDependencees(target, deep)
    }

    private fun buildDependencees(target: List<JClass>, deep: Int): List<JClass> {
        val container = ArrayList<JClass>()
        doBuildDependencees(target, deep, container)
        //TODO: clazz 存在重复可能有问题
        return container[0].dependencees
    }

    private fun doBuildDependencees(target: List<JClass>, deep: Int, container: MutableList<JClass>) {
        var pendingClasses = target.filterNot { container.contains(it) }
        if (pendingClasses.isEmpty() || deep == 0) {
            container.addAll(pendingClasses)
        } else {
            pendingClasses.forEach { it.dependencees = findDependencees(it) }
            container.addAll(pendingClasses)
            doBuildDependencees(pendingClasses.flatMap { it.dependencees }, deep - 1, container)
        }
    }

    private fun findDependencees(clazz: JClass): List<JClass> {
        clazz.id ?: throw ClassNoIdException("class $clazz no id found.")
        return repo.findDependencees(clazz.id)
    }

}
