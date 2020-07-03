package com.thoughtworks.archguard.module.domain.dubbo

import com.thoughtworks.archguard.module.domain.DependencyAnalysisHelper
import com.thoughtworks.archguard.module.domain.JClass
import com.thoughtworks.archguard.module.domain.NewDependency
import com.thoughtworks.archguard.module.domain.NewLogicModule
import com.thoughtworks.archguard.module.domain.SubModule
import com.thoughtworks.archguard.module.domain.getNewModule
import org.springframework.stereotype.Service

@Service
class DubboXmlDependencyAnalysisHelper(var xmlConfigService: XmlConfigService) : DependencyAnalysisHelper {

    override fun analysis(classDependency: NewDependency<JClass>, logicModules: List<NewLogicModule>, calleeModules: List<NewLogicModule>): List<NewLogicModule> {
        val calleeSubModuleByXml = xmlConfigService.getRealCalleeModuleByDependency(classDependency.caller, classDependency.callee)
        val xmlAnalysisModules = calleeSubModuleByXml.map { getNewModule(logicModules, SubModule(it.name)) }.flatten()
        return calleeModules.intersect(xmlAnalysisModules).toList()
    }
}