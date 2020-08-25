package com.thoughtworks.archguard.metrics.domain

import com.thoughtworks.archguard.module.domain.model.JClassVO

data class ClassNoc(val jClassVO: JClassVO, val nocValue: Int)
data class ClassDit(val jClassVO: JClassVO, val ditValue: Int)
data class ClassAbc(val jClassVO: JClassVO, val abcValue: Int)
data class ClassLCOM4(val jClassVO: JClassVO, val lcom4Value: Int)
