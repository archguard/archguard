package com.thoughtworks.archguard.module.domain

interface XmlConfigService {
    fun getRealCalleeModuleByDependency(callerClass: JClass, calleeClass: JClass): List<SubModule>
}