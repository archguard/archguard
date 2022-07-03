package com.thoughtworks.archguard.v2.frontier.clazz.domain.service

import com.thoughtworks.archguard.config.domain.ConfigureService
import com.thoughtworks.archguard.v2.frontier.clazz.domain.JClass
import com.thoughtworks.archguard.v2.frontier.clazz.domain.JClassRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ClassInvokeService(
    @Autowired private val repo: JClassRepository,
    @Autowired private val configureService: ConfigureService,
    @Autowired private val classConfigService: ClassConfigService,
) {
    private val log = LoggerFactory.getLogger(this.javaClass)

    fun findInvokes(
        systemId: Long,
        target: JClass,
        callerDeep: Int,
        calleeDeep: Int,
        needIncludeImpl: Boolean
    ): JClass {
        findClassCallers(systemId, target, callerDeep, needIncludeImpl)
        findClassCallees(systemId, target, calleeDeep, needIncludeImpl)
        return target
    }

    private fun findClassCallees(systemId: Long, target: JClass, deep: Int, needIncludeImpl: Boolean) {
        if (deep == 0 || target.module == null) return

        target.implements = if (!needIncludeImpl) emptyList() else {
            repo.findClassImplements(systemId, target.name, target.module)
                .filter { configureService.isDisplayNode(systemId, it.name) }
                .also { classConfigService.buildJClassColorConfig(it, systemId) }
        }

        target.callees = repo.findCallees(systemId, target.name, target.module)
            .filter { configureService.isDisplayNode(systemId, it.clazz.name) }
            .also { classConfigService.buildClassRelationColorConfig(it, systemId) }

        if (deep == 1) return
        target.implements.map { findClassCallees(systemId, it, deep - 1, needIncludeImpl) }
        target.callees.map { findClassCallees(systemId, it.clazz, deep - 1, needIncludeImpl) }
    }

    private fun findClassCallers(systemId: Long, target: JClass, deep: Int, needIncludeImpl: Boolean) {
        if (deep == 0) {
            return
        }
        if (target.module == null) {
            return
        }
        val parents = repo.findClassParents(systemId, target.module, target.name)
            .filter { configureService.isDisplayNode(systemId, it.name) }
        classConfigService.buildJClassColorConfig(parents, systemId)
        target.parents = parents

        val callers = repo.findCallers(systemId, target.name, target.module)
            .filter { configureService.isDisplayNode(systemId, it.clazz.name) }
        classConfigService.buildClassRelationColorConfig(callers, systemId)
        target.callers = callers
        if (deep == 1) {
            return
        } else {
            target.parents.map { findClassCallers(systemId, it, deep - 1, needIncludeImpl) }
            target.callers.map { findClassCallers(systemId, it.clazz, deep - 1, needIncludeImpl) }
        }
    }
}
