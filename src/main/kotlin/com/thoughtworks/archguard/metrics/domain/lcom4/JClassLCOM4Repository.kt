package com.thoughtworks.archguard.metrics.domain.lcom4

import com.thoughtworks.archguard.metrics.domain.ClassLCOM4

interface JClassLCOM4Repository {
    fun getClassLCOM4ExceedThreshold(systemId: Long, threshold: Integer): List<ClassLCOM4>
}