package com.thoughtworks.archguard.metrics.domain.lcom4

import com.thoughtworks.archguard.clazz.domain.JClass

interface LCOM4Service {
    fun calculateLCOM4(jClass: JClass): Int
}