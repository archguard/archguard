package com.thoughtworks.archguard.architecture.infrastructure

import com.thoughtworks.archguard.architecture.domain.repository.ArchSystemPO
import com.thoughtworks.archguard.architecture.domain.repository.ArchSystemRepository
import com.thoughtworks.archguard.code.module.domain.model.JClassVO
import com.thoughtworks.archguard.metrics.domain.MetricsRepository
import com.thoughtworks.archguard.metrics.domain.coupling.ClassCoupling
import com.thoughtworks.archguard.metrics.infrastructure.influx.ClassCouplingListInfluxDTO
import com.thoughtworks.archguard.metrics.infrastructure.influx.InfluxDBClient
import org.jdbi.v3.sqlobject.transaction.Transaction
import org.springframework.stereotype.Repository

@Repository
class ArchSystemRepositoryImpl(val influxDBClient: InfluxDBClient) : ArchSystemRepository {

    override fun getArchSystem(id: String): ArchSystemPO {
        TODO("Not yet implemented")
    }

    @Transaction
    override fun createArchSystem(archSystemPO: ArchSystemPO): String {
        TODO("Not yet implemented")
    }
}
