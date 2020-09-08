package com.thoughtworks.archgard.scanner2.infrastructure.persist

import com.thoughtworks.archgard.scanner2.domain.MetricRepository
import org.jdbi.v3.core.Jdbi
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class MetricRepositoryImpl(val classMetricsDao: ClassMetricsDao,
                           val jdbi: Jdbi) : MetricRepository {
    private val log = LoggerFactory.getLogger(MetricRepositoryImpl::class.java)

    @Transactional
    override fun insertOrUpdateClassMetricPOs(systemId: Long, classMetricPOs: List<ClassMetricPO>) {
        classMetricsDao.deleteBy(systemId)
        log.info("Delete project old data with id: {}", systemId)
        classMetricsDao.insert(classMetricPOs)
        log.info("Insert project new data with id: {}", systemId)
    }
}
