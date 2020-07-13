package com.thoughtworks.archguard.module.domain.dubbo

import com.thoughtworks.archguard.module.domain.model.JClass

interface XmlConfigService {
    fun getRealCalleeModuleByXmlConfig(callerClass: JClass, calleeClass: JClass): List<SubModuleDubbo>
}