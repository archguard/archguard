package com.thoughtworks.archguard.clazz.domain.service

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.clazz.exception.ClassNotFountException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ClassService {
    @Autowired
    private lateinit var classMethodCalleesService: ClassMethodCalleesService

    @Autowired
    private lateinit var classDependenceesService: ClassDependenceesService

    @Autowired
    private lateinit var classDependencerService: ClassDependencerService

    @Autowired
    private lateinit var repo: JClassRepository

    @Autowired
    private lateinit var classInvokeService: ClassInvokeService

    fun getDependencies(module: String, name: String, deep: Int): JClass {
        val target = getTargetClass(module, name)
        classDependenceesService.findDependencees(target, deep)
        classDependencerService.findDependencers(target, deep)
        return target
    }

    private fun getTargetClass(module: String, name: String): JClass {
        return repo.getJClassBy(name, module)
                ?: throw ClassNotFountException("Can't find class by module:${module}, class:${name}")
    }

    fun findInvokes(module: String, name: String, callerDeep: Int, calleeDeep: Int, needIncludeImpl: Boolean): JClass {
        val targetClass = getTargetClass(module, name)
        return classInvokeService.findInvokes(targetClass, callerDeep, calleeDeep, needIncludeImpl)
    }

    fun findMethodsCallees(module: String, name: String, calleeDeep: Int,
                           needIncludeImpl: Boolean, needParents: Boolean): JClass {
        val targetClass = getTargetClass(module, name)
        return classMethodCalleesService.findClassMethodsCallees(targetClass, calleeDeep,
                needIncludeImpl, needParents)
    }
}
