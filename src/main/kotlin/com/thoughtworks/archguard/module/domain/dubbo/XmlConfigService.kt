package com.thoughtworks.archguard.module.domain.dubbo

import com.thoughtworks.archguard.module.domain.model.JClass

interface XmlConfigService {
    fun getRealCalleeModuleByDependency(callerClass: JClass, calleeClass: JClass): List<SubModuleDubbo>
}