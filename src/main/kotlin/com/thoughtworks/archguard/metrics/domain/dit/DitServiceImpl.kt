package com.thoughtworks.archguard.metrics.domain.dit

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import org.springframework.stereotype.Service

@Service
class DitServiceImpl(val repo: JClassRepository) : DitService {
    override fun getDepthOfInheritance(systemId: Long, target: JClass): Int {
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
