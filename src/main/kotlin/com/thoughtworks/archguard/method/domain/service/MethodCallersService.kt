package com.thoughtworks.archguard.method.domain.service

import com.thoughtworks.archguard.method.domain.JMethod
import com.thoughtworks.archguard.method.domain.JMethodRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MethodCallersService {
    @Autowired
    private lateinit var repo: JMethodRepository
    fun findCallers(target: JMethod, deep: Int): JMethod {
        buildMethodCallers(listOf(target), deep)
        return target
    }

    fun buildMethodCallers(methods: List<JMethod>, deep: Int): List<JMethod> {
        val container = ArrayList<JMethod>()
        doBuildCallers(methods, deep, container)
        return methods
    }

    private fun doBuildCallers(methods: List<JMethod>, deep: Int, container: MutableList<JMethod>) {
        val pendindMethods = methods.filterNot { container.contains(it) }
        if (pendindMethods.isEmpty() || deep == 0) {
            container.addAll(pendindMethods)
        } else {
            pendindMethods.forEach {
                it.callers = repo.findMethodCallers(it.id)
            }
            doBuildCallers(pendindMethods.flatMap { it.callers }, deep - 1, container)
        }
    }
}
