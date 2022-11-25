package com.thoughtworks.archguard.metrics.infrastructure

import com.thoughtworks.archguard.code.module.domain.model.JClassVO
import com.thoughtworks.archguard.metrics.domain.MetricsRepository
import com.thoughtworks.archguard.metrics.domain.coupling.ClassCoupling
import com.thoughtworks.archguard.metrics.infrastructure.influx.ClassCouplingListInfluxDTO
import com.thoughtworks.archguard.metrics.infrastructure.influx.InfluxDBClient
import org.jdbi.v3.sqlobject.transaction.Transaction
import org.springframework.stereotype.Repository

@Repository
class MetricsRepositoryImpl(
    val influxDBClient: InfluxDBClient,
    val classCouplingWriteDao: ClassCouplingWriteDao,
    val classCouplingReadDao: ClassCouplingReadDao
) : MetricsRepository {

    @Transaction
    override fun insertAllClassCouplings(systemId: Long, classCouplings: List<ClassCoupling>) {
        classCouplingWriteDao.deleteBy(systemId)
        classCouplings.forEach {
            classCouplingWriteDao.insert(MetricClassCouplingWritePO.fromClassCoupling(systemId, it))
        }
        influxDBClient.save(ClassCouplingListInfluxDTO(systemId, classCouplings).toRequestBody())
    }

    override fun getClassCoupling(jClassVO: JClassVO): ClassCoupling? {
        val classCoupling = classCouplingReadDao.findClassCoupling(jClassVO.id!!) ?: return null

        return classCoupling.toClassCoupling()
    }

    override fun getClassCoupling(jClassVOs: List<JClassVO>): List<ClassCoupling> {
        return classCouplingReadDao.findClassCouplings(jClassVOs.map { it.id!! }).map { it.toClassCoupling() }
    }
}
