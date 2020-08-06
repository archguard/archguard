package com.thoughtworks.archguard.clazz.domain.service

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ClassMetricsService {
    @Autowired
    private lateinit var repo: JClassRepository

    fun getDepthOfInheritance(target: JClass): Int {
        val parents = repo.findClassParents(target.module, target.name)
        return findInheritanceDepth(parents)
    }

    private fun findInheritanceDepth(superClasses: List<JClass>): Int {
        if (superClasses.isEmpty()) {
            return 0
        }
        val deeperSuperClasses = mutableListOf<JClass>()
        for (superClass in superClasses) {
            deeperSuperClasses.addAll(repo.findClassParents(superClass.module, superClass.name))
        }
        return try {
            findInheritanceDepth(deeperSuperClasses) + 1
        } catch (ex: StackOverflowError) {
            System.err.println("Inheritance depth analysis step skipped due to memory overflow.")
            0
        }
    }
}
