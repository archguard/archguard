package com.thoughtworks.archgard.scanner2.infrastructure.mysql

import com.thoughtworks.archgard.scanner2.domain.model.ClassMetric
import com.thoughtworks.archgard.scanner2.domain.repository.ClassMetricRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class ClassClassMetricRepositoryImpl(val classMetricsDao: ClassMetricsDao) : ClassMetricRepository {
    private val log = LoggerFactory.getLogger(ClassClassMetricRepositoryImpl::class.java)

    override fun insertOrUpdateClassMetric(systemId: Long, classMetrics: List<ClassMetric>) {
        val classMetricPOs = classMetrics
                .map { ClassMetricPO(it.systemId, it.jClassVO.id!!, it.abc, it.dit, it.noc, it.lcom4, it.fanIn, it.fanOut) }

        classMetricsDao.deleteBy(systemId)
        log.info("Delete system class metric old data with id: {}", systemId)
        classMetricsDao.insert(classMetricPOs)
        log.info("Insert system class metric new data with id: {}", systemId)
    }

}
