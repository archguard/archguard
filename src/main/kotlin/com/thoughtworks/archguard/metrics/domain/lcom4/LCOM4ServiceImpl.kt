package com.thoughtworks.archguard.metrics.domain.lcom4

import com.thoughtworks.archguard.clazz.domain.JClass
import com.thoughtworks.archguard.clazz.domain.JClassRepository
import com.thoughtworks.archguard.method.domain.JMethodRepository
import org.springframework.stereotype.Service

@Service
class LCOM4ServiceImpl(val jClassRepository: JClassRepository, val jMethodRepository: JMethodRepository) : LCOM4Service {
    override fun calculateLCOM4(jClass: JClass): Int {
        TODO("Not yet implemented")
    }
}