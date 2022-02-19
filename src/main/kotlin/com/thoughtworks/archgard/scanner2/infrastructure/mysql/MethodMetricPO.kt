package com.thoughtworks.archgard.scanner2.infrastructure.mysql

data class MethodMetricPO(val systemId: Long, val methodId: String,
                          val fanIn: Int?, val fanOut: Int?)
