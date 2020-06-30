package com.thoughtworks.archguard.module.domain

interface XmlConfigService {
    fun getModuleByDependency(callerClass: JClass, calleeClass: JClass, modules: List<LogicModule>): List<SubModule>
}