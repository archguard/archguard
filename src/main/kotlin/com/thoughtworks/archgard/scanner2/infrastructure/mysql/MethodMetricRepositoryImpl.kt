package com.thoughtworks.archgard.scanner2.infrastructure.mysql

import com.thoughtworks.archgard.scanner2.domain.model.MethodMetric
import com.thoughtworks.archgard.scanner2.domain.repository.MethodMetricRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class MethodMetricRepositoryImpl(val methodMetricsDao: MethodMetricsDao) : MethodMetricRepository {
    private val log = LoggerFactory.getLogger(MethodMetricRepositoryImpl::class.java)

    override fun insertOrUpdateMethodMetric(systemId: Long, methodMetric: List<MethodMetric>) {
        val methodMetricPOs = methodMetric
                .map { MethodMetricPO(it.systemId, it.jMethodVO.id!!, it.fanIn, it.fanOut) }

        methodMetricsDao.deleteBy(systemId)
        log.info("Delete system method metric old data with id: {}", systemId)
        methodMetricsDao.insert(methodMetricPOs)
        log.info("Insert system method metric new data with id: {}", systemId)
    }
}