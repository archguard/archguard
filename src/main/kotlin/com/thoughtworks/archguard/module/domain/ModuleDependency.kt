package com.thoughtworks.archguard.module.domain

data class ModuleDependency(var caller: String, var callerClass: String, var callerMethod: String, var callee: String, var calleeClass: String, var calleeMethod: String)
