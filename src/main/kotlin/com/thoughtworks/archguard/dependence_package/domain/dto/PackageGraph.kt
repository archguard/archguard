package com.thoughtworks.archguard.dependence_package.domain.dto

data class PackageGraph(var nodes: List<PackageNode>, var edges: List<PackageEdge>)
