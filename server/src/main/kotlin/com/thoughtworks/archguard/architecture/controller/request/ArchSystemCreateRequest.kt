package com.thoughtworks.archguard.architecture.controller.request

import com.thoughtworks.archguard.architecture.domain.model.Architecture

data class ArchSystemCreateRequest(
    var name: String,
    var style: Architecture.ArchStyle
)
