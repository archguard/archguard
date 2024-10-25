package org.archguard.scanner.architecture.view.module.ddd

import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.Serializable

@Serializable
class DomainLayer(
    val aggregates: List<CodeDataStruct> = listOf(),
    val repositories: List<CodeDataStruct> = listOf(),
    val domainServices: List<CodeDataStruct> = listOf(),
    val factories: List<CodeDataStruct> = listOf()
)