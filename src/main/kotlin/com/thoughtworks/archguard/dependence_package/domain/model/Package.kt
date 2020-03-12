package com.thoughtworks.archguard.dependence_package.domain.model

data class Package(var id: Int, var name: String, var parent: Package?, var callee: Map<Package, Int>)
