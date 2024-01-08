package org.archguard.metric

import org.archguard.model.vos.JClassVO

data class ClassMetric(
    val systemId: Long,
    val jClassVO: JClassVO,
    val dit: Int?,
    val noc: Int?,
    val lcom4: Int?,
    val fanIn: Int?,
    val fanOut: Int?
)
