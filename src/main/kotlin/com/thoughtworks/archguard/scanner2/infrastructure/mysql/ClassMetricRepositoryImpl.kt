package com.thoughtworks.archguard.scanner2.infrastructure.mysql

import com.thoughtworks.archguard.scanner2.domain.model.ClassMetric
import com.thoughtworks.archguard.scanner2.domain.repository.ClassMetricRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class ClassMetricRepositoryImpl(val classMetricsDao: ClassMetricsDao) : ClassMetricRepository {
    private val log = LoggerFactory.getLogger(ClassMetricRepositoryImpl::class.java)

    override fun insertOrUpdateClassMetric(systemId: Long, classMetrics: List<ClassMetric>) {
        val classMetricPOs = classMetrics
                .map { ClassMetricPO(it.systemId, it.jClassVO.id!!, it.dit, it.noc, it.lcom4, it.fanIn, it.fanOut) }

        classMetricsDao.deleteBy(systemId)
        log.info("Delete system class metric old data with id: {}", systemId)
        if (classMetricPOs.isEmpty()) {
            log.warn("Insert system module metric new data with id is empty!: {}", systemId)
            return
        }
        classMetricsDao.insert(classMetricPOs)
        log.info("Insert system class metric new data with id: {}", systemId)
    }

}
