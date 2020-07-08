package com.thoughtworks.archguard.clazz.domain.service

import com.thoughtworks.archguard.clazz.exception.ClassNoIdException
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.module.domain.model.JClass
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ClassDependencerService {
    @Autowired
    private lateinit var repo: JClassRepository

    fun findDependencers(name: String, deep: Int): List<JClass> {
        val target = repo.getJClassByName(name)
        return buildDependencers(target, deep)
    }

    private fun buildDependencers(target: List<JClass>, deep: Int): List<JClass> {
        val container = ArrayList<JClass>()
        doBuildDependencers(target, deep, container)
        //TODO: clazz 存在重复可能有问题
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
        clazz.id ?: throw ClassNoIdException("class $clazz no id found.")
        return repo.findDependencers(clazz.id)
    }

}
