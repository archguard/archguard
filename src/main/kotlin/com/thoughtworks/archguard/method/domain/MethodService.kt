package com.thoughtworks.archguard.method.domain

import com.thoughtworks.archguard.config.domain.ConfigureService
import com.thoughtworks.archguard.method.domain.service.MethodCalleesService
import com.thoughtworks.archguard.method.domain.service.MethodCallersService
import org.springframework.stereotype.Service

@Service
class MethodService(val repo: JMethodRepository, val calleeService: MethodCalleesService,
                    val callerService: MethodCallersService, val configureService: ConfigureService) {
    fun findMethodCallees(systemId: Long, moduleName: String, clazzName: String, methodName: String, deep: Int, needIncludeImpl: Boolean): List<JMethod> {
        val target = getMethodBy(systemId, moduleName, clazzName, methodName).filter { configureService.isDisplayNode(systemId, it.name) }
        calleeService.findCallees(systemId, target, deep, needIncludeImpl)
        return target
    }

    fun findMethodCallers(systemId: Long, moduleName: String, clazzName: String, methodName: String, deep: Int): List<JMethod> {
        val target = getMethodBy(systemId, moduleName, clazzName, methodName).filter { configureService.isDisplayNode(systemId, it.name) }
        callerService.findCallers(systemId, target, deep)
        return target
    }


    fun findMethodInvokes(systemId: Long, moduleName: String, clazzName: String, methodName: String,
                          callerDeep: Int, calleeDeep: Int, needIncludeImpl: Boolean): List<JMethod> {
        val target = getMethodBy(systemId, moduleName, clazzName, methodName).filter { configureService.isDisplayNode(systemId, it.name) }
        callerService.findCallers(systemId, target, callerDeep)
        calleeService.findCallees(systemId, target, calleeDeep, needIncludeImpl)
        return target
    }

    private fun getMethodBy(systemId: Long, moduleName: String, clazzName: String, methodName: String): List<JMethod> {
        return repo.findMethodByModuleAndClazzAndName(systemId, moduleName, clazzName, methodName).filter { configureService.isDisplayNode(systemId, it.name) }
    }

    fun findMethodByModuleAndClazz(systemId: Long, clazzName: String, submoduleName: String): List<JMethod> {
        return repo.findMethodsByModuleAndClass(systemId, submoduleName, clazzName)
    }

}
