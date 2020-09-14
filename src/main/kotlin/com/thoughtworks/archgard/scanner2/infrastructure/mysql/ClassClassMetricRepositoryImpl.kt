package com.thoughtworks.archgard.scanner2.infrastructure.mysql

import com.thoughtworks.archgard.scanner2.domain.model.ClassMetric
import com.thoughtworks.archgard.scanner2.domain.repository.ClassMetricRepository
import org.jdbi.v3.core.Jdbi
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class ClassClassMetricRepositoryImpl(val classMetricsDao: ClassMetricsDao,
                                     val jdbi: Jdbi) : ClassMetricRepository {
    private val log = LoggerFactory.getLogger(ClassClassMetricRepositoryImpl::class.java)

    override fun insertOrUpdateClassMetric(systemId: Long, classMetric: List<ClassMetric>) {
        val classMetricPOs = classMetric
                .map { ClassMetricPO(it.systemId, it.jClassVO.id!!, it.abc, it.dit, it.noc, it.lcom4) }

        classMetricsDao.deleteBy(systemId)
        log.info("Delete project old data with id: {}", systemId)
        classMetricsDao.insert(classMetricPOs)
        log.info("Insert project new data with id: {}", systemId)
    }

}
