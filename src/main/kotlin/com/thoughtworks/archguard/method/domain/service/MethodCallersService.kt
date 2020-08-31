package com.thoughtworks.archguard.method.domain.service

import com.thoughtworks.archguard.config.domain.ConfigureService
import com.thoughtworks.archguard.method.domain.JMethod
import com.thoughtworks.archguard.method.domain.JMethodRepository
import org.springframework.stereotype.Service

@Service
class MethodCallersService(val repo: JMethodRepository, val configureService: ConfigureService) {
    fun findCallers(projectId: Long, target: List<JMethod>, deep: Int): List<JMethod> {
        buildMethodCallers(projectId, target, deep)
        return target
    }

    fun buildMethodCallers(projectId: Long, methods: List<JMethod>, deep: Int): List<JMethod> {
        val container = ArrayList<JMethod>()
        doBuildCallers(projectId, methods, deep, container)
        return methods
    }

    private fun doBuildCallers(projectId: Long, methods: List<JMethod>, deep: Int, container: MutableList<JMethod>) {
        val pendindMethods = methods.filterNot { container.contains(it) }
        if (pendindMethods.isEmpty() || deep == 0) {
            container.addAll(pendindMethods)
        } else {
            pendindMethods.forEach {
                it.callers = repo.findMethodCallers(it.id).filter { configureService.isDisplayNode(projectId, it.name) && configureService.isDisplayNode(projectId, it.clazz) }
            }
            doBuildCallers(projectId, pendindMethods.flatMap { it.callers }, deep - 1, container)
        }
    }
}
