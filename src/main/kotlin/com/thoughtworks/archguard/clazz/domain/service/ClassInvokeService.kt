package com.thoughtworks.archguard.clazz.domain.service

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.config.domain.ConfigureService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ClassInvokeService(val repo: JClassRepository, val configureService: ConfigureService) {
    private val log = LoggerFactory.getLogger(ClassInvokeService::class.java)

    fun findInvokes(projectId: Long, target: JClass, callerDeep: Int, calleeDeep: Int,
                    needIncludeImpl: Boolean): JClass {
        findClassCallers(projectId, target, callerDeep, needIncludeImpl)
        findClassCallees(projectId, target, calleeDeep, needIncludeImpl)
        return target
    }

    private fun findClassCallees(projectId: Long, target: JClass, deep: Int, needIncludeImpl: Boolean) {
        if (deep == 0) {
            return
        }
        if (target.module == "null") {
            return
        }
        var implements = listOf<JClass>()
        if (needIncludeImpl) {
            implements = repo.findClassImplements(projectId, target.name, target.module)
                    .filter { configureService.isDisplayNode(projectId, it.name) }
        }
        target.implements = implements
        target.callees = repo.findCallees(projectId, target.name, target.module)
                .filter { configureService.isDisplayNode(projectId, it.clazz.name) }
        if (deep == 1) {
            return
        } else {
            target.implements.map { findClassCallees(projectId, it, deep - 1, needIncludeImpl) }
            target.callees.map { findClassCallees(projectId, it.clazz, deep - 1, needIncludeImpl) }
        }
    }

    private fun findClassCallers(projectId: Long, target: JClass, deep: Int, needIncludeImpl: Boolean) {
        if (deep == 0) {
            return
        }
        if (target.module == "null") {
            return
        }
        target.parents = repo.findClassParents(projectId, target.module, target.name)
                .filter { configureService.isDisplayNode(projectId, it.name) }
        target.callers = repo.findCallers(projectId, target.name, target.module)
                .filter { configureService.isDisplayNode(projectId, it.clazz.name) }
        if (deep == 1) {
            return
        } else {
            target.parents.map { findClassCallers(projectId, it, deep - 1, needIncludeImpl) }
            target.callers.map { findClassCallers(projectId, it.clazz, deep - 1, needIncludeImpl) }
        }
    }


}
