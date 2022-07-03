package com.thoughtworks.archguard.v2.frontier.clazz.domain.service

import com.thoughtworks.archguard.v2.frontier.clazz.domain.CodeTree
import com.thoughtworks.archguard.v2.frontier.clazz.domain.JClass
import com.thoughtworks.archguard.v2.frontier.clazz.domain.JClassRepository
import com.thoughtworks.archguard.v2.frontier.clazz.exception.ClassNotFountException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ClassService(
    @Autowired private val classMethodCalleesService: ClassMethodCalleesService,
    @Autowired private val classDependenciesService: ClassDependenciesService,
    @Autowired private val classDependencerService: ClassDependencerService,
    @Autowired private val jClassRepository: JClassRepository,
    @Autowired private val classInvokeService: ClassInvokeService,
) {

    fun getDependencies(systemId: Long, module: String, name: String, deep: Int): JClass {
        val target = getTargetClass(systemId, module, name)
        classDependenciesService.findDependencies(systemId, target, deep)
        classDependencerService.findDependencers(systemId, target, deep)
        return target
    }

    private fun getTargetClass(systemId: Long, module: String, name: String): JClass {
        return jClassRepository.getJClassBy(systemId, name, module)
            ?: throw ClassNotFountException("Can't find class by module:$module, class:$name")
    }

    fun findInvokes(
        systemId: Long,
        module: String,
        name: String,
        callerDeep: Int,
        calleeDeep: Int,
        needIncludeImpl: Boolean
    ): JClass {
        val targetClass = getTargetClass(systemId, module, name)
        return classInvokeService.findInvokes(systemId, targetClass, callerDeep, calleeDeep, needIncludeImpl)
    }

    fun findMethodsCallees(
        systemId: Long,
        module: String,
        name: String,
        calleeDeep: Int,
        needIncludeImpl: Boolean,
        needParents: Boolean
    ): JClass {
        val targetClass = getTargetClass(systemId, module, name)
        return classMethodCalleesService.findClassMethodsCallees(
            systemId, targetClass,
            calleeDeep, needIncludeImpl, needParents
        )
    }

    fun initCodeTree(systemId: Long): CodeTree {
        val codeTree = CodeTree()
        jClassRepository.getAllBySystemId(systemId).filter { it.module != null }
            .map { codeTree.addClass(it.getFullName()) }
        codeTree.fixTopNodeSubModuleType()
        return codeTree
    }
}
