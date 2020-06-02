package com.thoughtworks.archguard.dependence.domain.logic_module

data class ModuleDependency(var callerClass: String, var callerMethod: String, var calleeClass: String, var calleeMethod: String)
