package com.thoughtworks.archguard.method.domain.service

import com.thoughtworks.archguard.config.domain.ConfigureService
import com.thoughtworks.archguard.method.domain.JMethod
import com.thoughtworks.archguard.method.domain.JMethodRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class MethodCalleesService(val repo: JMethodRepository, val configureService: ConfigureService) {
    private val log = LoggerFactory.getLogger(MethodCalleesService::class.java)


    fun buildMethodCallees(projectId: Long, methods: List<JMethod>, calleeDeep: Int, needIncludeImpl: Boolean): List<JMethod> {
        val container = ArrayList<JMethod>()
        // FIXME Cost too much time
        doBuildCallees(projectId, methods, calleeDeep, container, needIncludeImpl)
        return methods
    }

    private fun doBuildCallees(projectId: Long, methods: List<JMethod>, calleeDeep: Int, container: MutableList<JMethod>, needIncludeImpl: Boolean) {
        val pendindMethods = methods.filterNot { container.contains(it) }
        if (pendindMethods.isEmpty() || calleeDeep == 0) {
            container.addAll(pendindMethods)
        } else {
            pendindMethods.parallelStream().forEach {
                it.callees = repo.findMethodCallees(it.id).filter { configureService.isDisplayNode(projectId, it.name) && configureService.isDisplayNode(projectId, it.clazz) }
                if (needIncludeImpl) {
                    it.implements = repo.findMethodImplements(it.id, it.name).filter { configureService.isDisplayNode(projectId, it.name) && configureService.isDisplayNode(projectId, it.clazz) }
                }
            }
            doBuildCallees(projectId,
                    pendindMethods.flatMap { it.callees }, calleeDeep - 1, container, needIncludeImpl)
            doBuildCallees(projectId,
                    pendindMethods.flatMap { it.implements }, calleeDeep, container, needIncludeImpl)
        }
    }

    fun findCallees(projectId: Long, target: List<JMethod>, deep: Int, needIncludeImpl: Boolean): List<JMethod> {
        return buildMethodCallees(projectId, target, deep, needIncludeImpl)
    }
}
