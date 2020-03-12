package com.thoughtworks.archguard.dependence_package.domain.dto

data class PackageGraph(var packageNodes: List<PackageNode>, var packageEdges: List<PackageEdge>)
