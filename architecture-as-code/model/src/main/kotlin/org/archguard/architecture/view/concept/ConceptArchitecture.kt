package org.archguard.architecture.view.concept

import kotlinx.serialization.Serializable

/**
 * A module in Maven or Gradle should focus on single responsibility in concept level.
 * So it should be a CComponent or a CConnector.
 * If it has more than one identity，may need be divided to more than one module.
 */
@Serializable
class ConceptArchitecture(
    val type: ConceptType = ConceptType.CComponent,
    val domainModels: List<DomainModel> = listOf(),
    val reliability: Float = 0.0f,
    val desc: String = "",
)

