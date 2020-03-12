package com.thoughtworks.archguard.dependence_package.domain.dto

data class ModuleGraph(var nodes:List<ModuleNode>, var edges: List<ModuleEdge>)
