package com.thoughtworks.archguard.module.domain.dubbo

import com.thoughtworks.archguard.module.domain.DependencyAnalysisHelper
import com.thoughtworks.archguard.module.domain.JClass
import com.thoughtworks.archguard.module.domain.Dependency
import com.thoughtworks.archguard.module.domain.LogicModule
import com.thoughtworks.archguard.module.domain.SubModule
import com.thoughtworks.archguard.module.domain.getModule
import org.springframework.stereotype.Service

@Service
class DubboXmlDependencyAnalysisHelper(var xmlConfigService: XmlConfigService) : DependencyAnalysisHelper {

    override fun analysis(classDependency: Dependency<JClass>, logicModules: List<LogicModule>, calleeModules: List<LogicModule>): List<LogicModule> {
        val calleeSubModuleByXml = xmlConfigService.getRealCalleeModuleByDependency(classDependency.caller, classDependency.callee)
        val xmlAnalysisModules = calleeSubModuleByXml.map { getModule(logicModules, SubModule(it.name)) }.flatten()
        return calleeModules.intersect(xmlAnalysisModules).toList()
    }
}