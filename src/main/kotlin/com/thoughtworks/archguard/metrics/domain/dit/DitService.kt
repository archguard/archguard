package com.thoughtworks.archguard.metrics.domain.dit

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.metrics.domain.ClassDit
import org.springframework.stereotype.Service

@Service
class DitService(val repo: JClassRepository) {

    fun calculateAllDit(systemId: Long): List<ClassDit> {
        val jClasses = repo.getJClassesHasModules(systemId)
        val classDitList = mutableListOf<ClassDit>()
        jClasses.forEach { classDitList.add(ClassDit(it.toVO(), getDepthOfInheritance(systemId, it))) }
        return classDitList
    }

    fun getDepthOfInheritance(systemId: Long, target: JClass): Int {
        val parents = repo.findClassParents(systemId, target.module, target.name)
        return findInheritanceDepth(systemId, parents)
    }

    private fun findInheritanceDepth(systemId: Long, superClasses: List<JClass>): Int {
        if (superClasses.isEmpty()) {
            return 0
        }
        val deeperSuperClasses = mutableListOf<JClass>()
        for (superClass in superClasses) {
            deeperSuperClasses.addAll(repo.findClassParents(systemId, superClass.module, superClass.name))
        }
        return try {
            findInheritanceDepth(systemId, deeperSuperClasses) + 1
        } catch (ex: StackOverflowError) {
            System.err.println("Inheritance depth analysis step skipped due to memory overflow.")
            0
        }
    }
}
