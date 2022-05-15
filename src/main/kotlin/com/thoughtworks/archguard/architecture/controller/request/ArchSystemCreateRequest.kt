package com.thoughtworks.archguard.architecture.controller.request

import com.thoughtworks.archguard.architecture.domain.model.ArchStyle

data class ArchSystemCreateRequest(
    var name: String,
    var style: ArchStyle
)
