package com.thoughtworks.archguard.dependence_package.domain.model

data class Module(var id: Int, var name: String, var callee: Map<Module, Int>)
