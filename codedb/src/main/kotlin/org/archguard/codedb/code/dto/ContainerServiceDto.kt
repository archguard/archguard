package org.archguard.codedb.code.dto

import kotlinx.serialization.Serializable
import org.archguard.codedb.code.domain.ContainerDemand
import org.archguard.codedb.code.domain.ContainerSupply

@Serializable
data class ContainerServiceDto(
    var name: String = "",
    var demands: List<ContainerDemand> = listOf(),
    var resources: List<ContainerSupply> = listOf()
)