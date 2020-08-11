package com.thoughtworks.archguard.metrics.domain.abc

import com.thoughtworks.archguard.clazz.domain.JClass
import org.springframework.stereotype.Service

@Service
class AbcServiceImpl : AbcService {
    override fun calculateAbc(jClass: JClass): Int {
        TODO("Not yet implemented")
    }
}