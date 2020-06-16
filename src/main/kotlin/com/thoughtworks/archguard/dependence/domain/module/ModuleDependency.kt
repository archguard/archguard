package com.thoughtworks.archguard.dependence.domain.module

data class ModuleDependency(var caller: String, var callerClass: String, var callerMethod: String, var callee: String, var calleeClass: String, var calleeMethod: String)
