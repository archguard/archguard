package com.thoughtworks.archguard.clazz.domain.service

import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.clazz.exception.ClassNotFountException
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClass
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ClassService {
    @Autowired
    private lateinit var classDependenceesService: ClassDependenceesService

    @Autowired
    private lateinit var classDependencerService: ClassDependencerService

    @Autowired
    private lateinit var repo: JClassRepository

    @Autowired
    private lateinit var classInvokeService: ClassInvokeService
    fun findDependencies(module: String, name: String, deep: Int): Dependency<List<JClass>> {
        val target = getTargetClass(module, name)
        val callees = classDependenceesService.findDependencees(target, deep)
        val callers = classDependencerService.findDependencers(target, deep)
        return Dependency(callers, callees)
    }

    private fun getTargetClass(module: String, name: String): MutableList<JClass> {
        val target = mutableListOf<JClass>()
        if (module.isEmpty()) {
            target.addAll(repo.getJClassByName(name))
        } else {
            target.add(repo.getJClassBy(name, module)
                    ?: throw ClassNotFountException("Can't find class by module:${module}, class:${name}"))
        }
        if (target.isEmpty()) {
            throw ClassNotFountException("Can't find class by module:${module}, class:${name}")
        }
        return target
    }

    fun findInvokes(module: String, name: String, callerDeep: Int, calleeDeep: Int, needIncludeImpl: Boolean): JClass {
        val targetClass = getTargetClass(module, name)[0]
        return classInvokeService.findInvokes(targetClass, callerDeep, calleeDeep, needIncludeImpl)
    }

}
