package com.thoughtworks.archguard.method.domain.service

import com.thoughtworks.archguard.config.domain.ConfigureService
import com.thoughtworks.archguard.method.domain.JMethod
import com.thoughtworks.archguard.method.domain.JMethodRepository
import org.springframework.stereotype.Service

@Service
class MethodCallersService(val repo: JMethodRepository, val configureService: ConfigureService) {
    fun findCallers(systemId: Long, target: List<JMethod>, deep: Int): List<JMethod> {
        buildMethodCallers(systemId, target, deep)
        return target
    }

    fun buildMethodCallers(systemId: Long, methods: List<JMethod>, deep: Int): List<JMethod> {
        val container = ArrayList<JMethod>()
        doBuildCallers(systemId, methods, deep, container)
        return methods
    }

    private fun doBuildCallers(systemId: Long, methods: List<JMethod>, deep: Int, container: MutableList<JMethod>) {
        val pendindMethods = methods.filterNot { container.contains(it) }
        if (pendindMethods.isEmpty() || deep == 0) {
            container.addAll(pendindMethods)
        } else {
            pendindMethods.forEach {
                it.callers = repo.findMethodCallers(it.id).filter { configureService.isDisplayNode(systemId, it.name) && configureService.isDisplayNode(systemId, it.clazz) }
            }
            doBuildCallers(systemId, pendindMethods.flatMap { it.callers }, deep - 1, container)
        }
    }
}
