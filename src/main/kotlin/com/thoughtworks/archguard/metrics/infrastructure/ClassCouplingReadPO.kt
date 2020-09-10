package com.thoughtworks.archguard.metrics.infrastructure

import com.thoughtworks.archguard.metrics.domain.coupling.ClassCoupling
import com.thoughtworks.archguard.module.domain.model.JClassVO

class ClassCouplingReadPO(var classId: String,
                          var systemId: Int,
                          var name: String,
                          var module: String,
                          var innerFanIn: Int,
                          var innerFanOut: Int,
                          var outerFanIn: Int,
                          var outerFanOut: Int) {
    constructor() : this("", 0, "", "", 0, 0, 0, 0)

    fun toClassCoupling(): ClassCoupling {
        val jClassVO = JClassVO(name, module)
        jClassVO.id = classId
        return ClassCoupling(jClassVO, innerFanIn, innerFanOut, outerFanIn, outerFanOut)
    }
}