package com.thoughtworks.archguard.module.domain.dubbo

import com.thoughtworks.archguard.module.domain.getModule
import com.thoughtworks.archguard.module.domain.model.*
import org.springframework.stereotype.Service

@Service
class DubboXmlDependencyAnalysisHelper(val xmlConfigService: XmlConfigService){

    fun analysis(classDependency: Dependency<JClassVO>, logicModules: List<LogicModule>): List<LogicModule> {
        val calleeSubModuleByXml = xmlConfigService.getRealCalleeModuleByXmlConfig(classDependency.caller, classDependency.callee)
        return calleeSubModuleByXml.map { getModule(logicModules, SubModule(it.name)) }
                .flatten().toSet().toList()
    }
}
