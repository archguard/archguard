package com.thoughtworks.archguard.module.domain.dubbo

import com.thoughtworks.archguard.module.domain.model.JClassVO

interface XmlConfigService {
    fun getRealCalleeModuleByXmlConfig(callerClass: JClassVO, calleeClass: JClassVO): List<SubModuleDubbo>
}
