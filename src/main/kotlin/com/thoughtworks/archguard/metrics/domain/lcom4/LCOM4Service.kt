package com.thoughtworks.archguard.metrics.domain.lcom4

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.module.domain.graph.GraphStore

interface LCOM4Service {
    fun getLCOM4Graph(jClass: JClass): GraphStore
}