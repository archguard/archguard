package com.thoughtworks.archguard.dependence_module.domain.dto

data class ModuleGraph(var nodes:List<ModuleNode>, var edges: List<ModuleEdge>)
