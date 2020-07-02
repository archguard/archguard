package com.thoughtworks.archguard.module.domain.dubbo

import com.thoughtworks.archguard.module.domain.Dependency
import com.thoughtworks.archguard.module.domain.DependencyAnalysisHelper
import com.thoughtworks.archguard.module.domain.JClass
import com.thoughtworks.archguard.module.domain.LogicModule
import com.thoughtworks.archguard.module.domain.getModule
import org.springframework.stereotype.Service

@Service
class DubboXmlDependencyAnalysisHelper(var xmlConfigService: XmlConfigService) : DependencyAnalysisHelper {

    override fun analysis(classDependency: Dependency, logicModules: List<LogicModule>, calleeModules: List<String>): List<String> {
        val callerJClass = JClass(classDependency.caller.split(".").subList(1, classDependency.caller.split(".").size).joinToString("."), classDependency.caller.split(".")[0])
        val calleeJClass = JClass(classDependency.callee.split(".").subList(1, classDependency.callee.split(".").size).joinToString("."), classDependency.callee.split(".")[0])
        val calleeSubModuleByXml = xmlConfigService.getRealCalleeModuleByDependency(callerJClass, calleeJClass)
        val xmlAnalysisModules = calleeSubModuleByXml.map { getModule(logicModules, it.name) }.flatten()
        return calleeModules.intersect(xmlAnalysisModules).toList()
    }
}