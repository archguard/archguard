package com.thoughtworks.archguard.metrics.domain.noc

import com.thoughtworks.archguard.clazz.domain.JClass

interface NocService {
    fun getNoc(systemId: Long, jClass: JClass): Int
}
