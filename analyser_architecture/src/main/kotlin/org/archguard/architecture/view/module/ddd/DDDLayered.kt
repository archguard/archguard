package org.archguard.architecture.view.module.ddd

import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.Serializable

@Serializable
class DomainLayer(
    val aggregates: List<CodeDataStruct>,
    val repositories: List<CodeDataStruct>,
    val domainServices: List<CodeDataStruct>,
    val factories: List<CodeDataStruct>
)