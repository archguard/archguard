package org.archguard.metric

import org.archguard.model.vos.JMethodVO

data class MethodMetric(
    val systemId: Long,
    val jMethodVO: JMethodVO,
    val fanIn: Int?,
    val fanOut: Int?
)
