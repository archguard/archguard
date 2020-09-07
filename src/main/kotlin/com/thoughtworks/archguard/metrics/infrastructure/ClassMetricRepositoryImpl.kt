package com.thoughtworks.archguard.metrics.infrastructure

import com.thoughtworks.archguard.metrics.appl.ClassMetricPO
import com.thoughtworks.archguard.metrics.domain.ClassMetricRepository
import org.jdbi.v3.core.Jdbi
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class ClassMetricRepositoryImpl(val classMetricsDao: ClassMetricsDao,
                                val jdbi: Jdbi) : ClassMetricRepository {
    private val log = LoggerFactory.getLogger(ClassMetricRepositoryImpl::class.java)

    @Transactional
    override fun insertOrUpdateClassMetricPOs(systemId: Long, classMetricPOs: List<ClassMetricPO>) {
        classMetricsDao.deleteBy(systemId)
        log.info("Delete project old data with id: {}", systemId)
        classMetricsDao.insert(classMetricPOs)
        log.info("Insert project new data with id: {}", systemId)
    }
}
