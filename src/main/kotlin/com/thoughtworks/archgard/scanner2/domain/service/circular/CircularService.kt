package com.thoughtworks.archgard.scanner2.domain.service.circular

import com.thoughtworks.archgard.scanner2.domain.model.CircularDependenciesCount
import com.thoughtworks.archgard.scanner2.infrastructure.influx.CircularDependenciesCountDtoForWriteInfluxDB
import com.thoughtworks.archgard.scanner2.infrastructure.influx.InfluxDBClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CircularService(private val moduleCircularProcessor: ModuleCircularProcessor,
                      private val packageCircularProcessor: PackageCircularProcessor,
                      private val classCircularProcessor: ClassCircularProcessor,
                      private val methodCircularProcessor: MethodCircularProcessor,
                      private val influxDBClient: InfluxDBClient) {

    private val log = LoggerFactory.getLogger(CircularService::class.java)

    @Transactional
    fun persistCircularDependencyMetrics(systemId: Long) {
        val moduleCircularDependency = moduleCircularProcessor.process(systemId)
        val packageCircularDependency = packageCircularProcessor.process(systemId)
        val classCircularDependency = classCircularProcessor.process(systemId)
        val methodCircularDependency = methodCircularProcessor.process(systemId)

        val circularDependenciesCount = CircularDependenciesCount(systemId, moduleCircularDependency.size,
                packageCircularDependency.size, classCircularDependency.size, methodCircularDependency.size)
        influxDBClient.save(CircularDependenciesCountDtoForWriteInfluxDB(circularDependenciesCount).toRequestBody())
        log.info("-----------------------------------------------------------------------")
        log.info("Finished persist circularDependenciesCount to influxdb in systemId $systemId")
        log.info("-----------------------------------------------------------------------")
    }
}