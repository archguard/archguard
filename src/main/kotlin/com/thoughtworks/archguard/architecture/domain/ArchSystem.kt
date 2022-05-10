package com.thoughtworks.archguard.architecture.domain

data class ArchSystem(
    var id: String,
    var name: String,
    var subSystems: List<ArchSystem>,
)
