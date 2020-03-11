package com.thoughtworks.archguard.dependence_package.domain.dto

data class PackageGraph(var nodes: List<Node>, var edges: List<Edge>)
