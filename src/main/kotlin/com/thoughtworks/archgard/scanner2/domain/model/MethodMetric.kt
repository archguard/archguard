package com.thoughtworks.archgard.scanner2.domain.model

data class MethodMetric(val systemId: Long, val jMethodVO: JMethodVO,
                        val fanIn: Int?, val fanOut: Int?)