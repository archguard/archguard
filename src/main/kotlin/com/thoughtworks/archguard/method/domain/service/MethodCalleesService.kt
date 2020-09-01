package com.thoughtworks.archguard.method.domain.service

import com.thoughtworks.archguard.config.domain.ConfigureService
import com.thoughtworks.archguard.method.domain.JMethod
import com.thoughtworks.archguard.method.domain.JMethodRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class MethodCalleesService(val repo: JMethodRepository, val configureService: ConfigureService, val methodConfigService: MethodConfigService) {
    private val log = LoggerFactory.getLogger(MethodCalleesService::class.java)


    fun buildMethodCallees(systemId: Long, methods: List<JMethod>, calleeDeep: Int, needIncludeImpl: Boolean): List<JMethod> {
        val container = ArrayList<JMethod>()
        // FIXME Cost too much time
        doBuildCallees(systemId, methods, calleeDeep, container, needIncludeImpl)
        return methods
    }

    private fun doBuildCallees(systemId: Long, methods: List<JMethod>, calleeDeep: Int, container: MutableList<JMethod>, needIncludeImpl: Boolean) {
        val pendindMethods = methods.filterNot { container.contains(it) }
        if (pendindMethods.isEmpty() || calleeDeep == 0) {
            container.addAll(pendindMethods)
        } else {
            pendindMethods.parallelStream().forEach {
                val callees = repo.findMethodCallees(it.id).filter { configureService.isDisplayNode(systemId, it.name) && configureService.isDisplayNode(systemId, it.clazz) }
                methodConfigService.buildColorConfig(callees, systemId)
                it.callees = callees
                if (needIncludeImpl) {
                    val implements = repo.findMethodImplements(it.id, it.name).filter { configureService.isDisplayNode(systemId, it.name) && configureService.isDisplayNode(systemId, it.clazz) }
                    methodConfigService.buildColorConfig(implements, systemId)
                    it.implements = implements
                }
            }
            doBuildCallees(systemId,
                    pendindMethods.flatMap { it.callees }, calleeDeep - 1, container, needIncludeImpl)
            doBuildCallees(systemId,
                    pendindMethods.flatMap { it.implements }, calleeDeep, container, needIncludeImpl)
        }
    }

    fun findCallees(systemId: Long, target: List<JMethod>, deep: Int, needIncludeImpl: Boolean): List<JMethod> {
        return buildMethodCallees(systemId, target, deep, needIncludeImpl)
    }
}
