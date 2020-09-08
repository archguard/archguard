package com.thoughtworks.archgard.scanner2.infrastructure.persist

import com.thoughtworks.archgard.scanner2.domain.model.ClassMetric
import com.thoughtworks.archgard.scanner2.domain.repository.MetricRepository
import org.jdbi.v3.core.Jdbi
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class MetricRepositoryImpl(val classMetricsDao: ClassMetricsDao,
                           val jdbi: Jdbi) : MetricRepository {
    private val log = LoggerFactory.getLogger(MetricRepositoryImpl::class.java)

    @Transactional
    override fun insertOrUpdateClassMetric(systemId: Long, classMetric: List<ClassMetric>) {
        val classMetricPOs = classMetric
                .map { ClassMetricPO(it.systemId, it.jClassVO.id!!, it.abc, it.dit, it.noc, it.lcom4) }

        classMetricsDao.deleteBy(systemId)
        log.info("Delete project old data with id: {}", systemId)
        classMetricsDao.insert(classMetricPOs)
        log.info("Insert project new data with id: {}", systemId)
    }

}
