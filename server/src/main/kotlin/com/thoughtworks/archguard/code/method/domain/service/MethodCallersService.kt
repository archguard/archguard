package com.thoughtworks.archguard.code.method.domain.service

import com.thoughtworks.archguard.code.method.domain.JMethod
import com.thoughtworks.archguard.code.method.domain.JMethodRepository
import com.thoughtworks.archguard.config.domain.ConfigureService
import org.springframework.stereotype.Service

@Service
class MethodCallersService(val repo: JMethodRepository, val configureService: ConfigureService, val methodConfigService: MethodConfigService) {
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
                val callers = repo.findMethodCallers(it.id).filter { configureService.isDisplayNode(systemId, it.name) && configureService.isDisplayNode(systemId, it.clazz) }
                methodConfigService.buildColorConfig(callers, systemId)
                it.callers = callers
            }
            doBuildCallers(systemId, pendindMethods.flatMap { it.callers }, deep - 1, container)
        }
    }
}
