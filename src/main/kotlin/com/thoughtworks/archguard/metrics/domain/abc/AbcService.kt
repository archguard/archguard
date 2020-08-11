package com.thoughtworks.archguard.metrics.domain.abc

import com.thoughtworks.archguard.clazz.domain.JClass

interface AbcService {
    fun calculateAbc(jClass: JClass): Int
}