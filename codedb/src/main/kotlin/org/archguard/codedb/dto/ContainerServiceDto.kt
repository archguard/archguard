package org.archguard.codedb.dto

import kotlinx.serialization.Serializable
import org.archguard.codedb.domain.ContainerDemand
import org.archguard.codedb.domain.ContainerSupply

@Serializable
data class ContainerServiceDto(
    var name: String = "",
    var demands: List<ContainerDemand> = listOf(),
    var resources: List<ContainerSupply> = listOf()
)