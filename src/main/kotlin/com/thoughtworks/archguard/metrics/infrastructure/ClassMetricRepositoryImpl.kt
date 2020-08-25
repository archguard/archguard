package com.thoughtworks.archguard.metrics.infrastructure

import com.thoughtworks.archguard.metrics.domain.ClassMetricPO
import com.thoughtworks.archguard.metrics.domain.ClassMetricRepository
import org.springframework.stereotype.Repository

@Repository
class ClassMetricRepositoryImpl : ClassMetricRepository {
    override fun insertClassMetricPOs(classMetricPOs: List<ClassMetricPO>) {
        TODO("Not yet implemented")
    }
}