package com.thoughtworks.archguard.module.domain.dubbo

import com.thoughtworks.archguard.module.domain.DependencyAnalysisHelper
import com.thoughtworks.archguard.module.domain.getModule
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClass
import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.domain.model.SubModule
import org.springframework.stereotype.Service

@Service
class DubboXmlDependencyAnalysisHelper(var xmlConfigService: XmlConfigService) : DependencyAnalysisHelper {

    override fun analysis(classDependency: Dependency<JClass>, logicModules: List<LogicModule>, calleeModules: List<LogicModule>): List<LogicModule> {
        val calleeSubModuleByXml = xmlConfigService.getRealCalleeModuleByDependency(classDependency.caller, classDependency.callee)
        val xmlAnalysisModules = calleeSubModuleByXml.map { getModule(logicModules, SubModule(it.name)) }.flatten()
        return calleeModules.intersect(xmlAnalysisModules).toList()
    }
}