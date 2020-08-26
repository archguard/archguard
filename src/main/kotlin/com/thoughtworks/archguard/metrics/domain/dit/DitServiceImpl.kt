package com.thoughtworks.archguard.metrics.domain.dit

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import org.springframework.stereotype.Service

@Service
class DitServiceImpl(val repo: JClassRepository) : DitService {
    override fun getDepthOfInheritance(projectId: Long, target: JClass): Int {
        val parents = repo.findClassParents(projectId, target.module, target.name)
        return findInheritanceDepth(projectId, parents)
    }

    private fun findInheritanceDepth(projectId: Long, superClasses: List<JClass>): Int {
        if (superClasses.isEmpty()) {
            return 0
        }
        val deeperSuperClasses = mutableListOf<JClass>()
        for (superClass in superClasses) {
            deeperSuperClasses.addAll(repo.findClassParents(projectId, superClass.module, superClass.name))
        }
        return try {
            findInheritanceDepth(projectId, deeperSuperClasses) + 1
        } catch (ex: StackOverflowError) {
            System.err.println("Inheritance depth analysis step skipped due to memory overflow.")
            0
        }
    }
}
