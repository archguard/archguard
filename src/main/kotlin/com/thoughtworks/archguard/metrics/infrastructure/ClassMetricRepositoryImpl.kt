package com.thoughtworks.archguard.metrics.infrastructure

import com.thoughtworks.archguard.metrics.appl.ClassMetricPO
import com.thoughtworks.archguard.metrics.domain.ClassDit
import com.thoughtworks.archguard.metrics.domain.ClassLCOM4
import com.thoughtworks.archguard.metrics.domain.ClassMetricRepository
import com.thoughtworks.archguard.module.domain.dubbo.SubModuleDubbo
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
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

    override fun getClassLCOM4ExceedThreshold(systemId: Long, threshold: Int): List<ClassLCOM4> {
        val sql = "select c.system_id, c.name, c.module, m.lcom4 from JClass c \n" +
                "inner join class_metrics m on m.class_id = c.id \n" +
                "where c.system_id =:system_id and m.lcom4 > :lcom4;"
        val classLCOM4POList = jdbi.withHandle<List<ClassLCOM4PO>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(ClassLCOM4PO::class.java))
            it.createQuery(sql)
                    .bind("system_id", systemId)
                    .bind("lcom4", threshold)
                    .mapTo(ClassLCOM4PO::class.java)
                    .list()
        }
        return classLCOM4POList.map { it.toClassLCOM4() }
    }

    override fun getClassDitExceedThreshold(systemId: Long, threshold: Int): List<ClassDit> {
        val sql = "select c.system_id, c.name, c.module, m.dit from JClass c \n" +
                "inner join class_metrics m on m.class_id = c.id \n" +
                "where c.system_id =:system_id and m.dit > :dit;"
        val classDitPOList = jdbi.withHandle<List<ClassDitPO>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(ClassDitPO::class.java))
            it.createQuery(sql)
                    .bind("system_id", systemId)
                    .bind("dit", threshold)
                    .mapTo(ClassDitPO::class.java)
                    .list()
        }
        return classDitPOList.map { it.toClassDit() }
    }
}
