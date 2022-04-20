package org.archguard.rule.impl.container.model

import kotlinx.serialization.Serializable

@Serializable
data class ContainerResource(
    var sourceUrl: String = "",
    var sourceHttpMethod: String = "",
    var packageName: String = "",
    var className: String = "",
    var methodName: String = ""
)