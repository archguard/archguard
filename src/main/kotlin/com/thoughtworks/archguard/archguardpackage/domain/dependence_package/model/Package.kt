package com.thoughtworks.archguard.archguardpackage.domain.model

data class Package(var id: Int, var name: String, var parent: Package?, var callee: List<Pair<Package, Int>>)
