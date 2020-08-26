package com.thoughtworks.archguard.metrics.infrastructure

import com.thoughtworks.archguard.metrics.domain.ClassMetricPO
import com.thoughtworks.archguard.metrics.domain.ClassMetricRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class ClassMetricRepositoryImpl(val classMetricsDao: ClassMetricsDao) : ClassMetricRepository {
    private val log = LoggerFactory.getLogger(ClassMetricRepositoryImpl::class.java)

    @Transactional
    override fun insertOrUpdateClassMetricPOs(projectId: Long, classMetricPOs: List<ClassMetricPO>) {
        classMetricsDao.deleteBy(projectId)
        log.info("Delete project old data with id: {}", projectId)
        classMetricsDao.insert(classMetricPOs)
        log.info("Insert project new data with id: {}", projectId)
    }
}