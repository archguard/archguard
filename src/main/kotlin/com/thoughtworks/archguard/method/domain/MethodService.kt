package com.thoughtworks.archguard.method.domain

import com.thoughtworks.archguard.method.domain.service.MethodCalleesService
import com.thoughtworks.archguard.method.domain.service.MethodCallersService
import com.thoughtworks.archguard.method.exception.JMethodNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MethodService {

    @Autowired
    private lateinit var repo: JMethodRepository

    @Autowired
    private lateinit var calleeService: MethodCalleesService

    @Autowired
    private lateinit var callerService: MethodCallersService
    fun findMethodCallees(moduleName: String, clazzName: String, methodName: String, deep: Int, needIncludeImpl: Boolean): JMethod {
        val target = getMethodBy(moduleName, clazzName, methodName)
        calleeService.findCallees(target, deep, needIncludeImpl)
        return target
    }

    fun findMethodCallers(moduleName: String, clazzName: String, methodName: String, deep: Int): JMethod {
        val target = getMethodBy(moduleName, clazzName, methodName)
        callerService.findCallers(target, deep)
        return target
    }


    fun findMethodInvokes(moduleName: String, clazzName: String, methodName: String,
                          callerDeep: Int, calleeDeep: Int, needIncludeImpl: Boolean): JMethod {
        val target = getMethodBy(moduleName, clazzName, methodName)
        callerService.findCallers(target, callerDeep)
        calleeService.findCallees(target, calleeDeep, needIncludeImpl)
        return target
    }

    private fun getMethodBy(moduleName: String, clazzName: String, methodName: String): JMethod {
        if (moduleName.isEmpty()) {
            return repo.findMethodByClazzAndName(clazzName, methodName)
                    ?: throw JMethodNotFoundException("Can't found method by class:'$clazzName', method:'$methodName'")
        }
        return repo.findMethodByModuleAndClazzAndName(moduleName, clazzName, methodName)
                ?: throw JMethodNotFoundException("Can't found method by module: '$moduleName', class:'$clazzName', method:'$methodName'")
    }

}
