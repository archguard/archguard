package com.thoughtworks.archguard.metrics.infrastructure

import com.thoughtworks.archguard.metrics.domain.ClassLCOM4
import com.thoughtworks.archguard.metrics.domain.lcom4.JClassLCOM4Repository
import com.thoughtworks.archguard.module.domain.dubbo.ServiceConfig
import com.thoughtworks.archguard.module.domain.dubbo.SubModuleDubbo
import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.infrastructure.Utils
import com.thoughtworks.archguard.module.infrastructure.dubbo.ServiceConfigDto
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class JClassLCOM4RepositoryImpl(val jdbi: Jdbi) : JClassLCOM4Repository {

    private val log = LoggerFactory.getLogger(ClassMetricRepositoryImpl::class.java)

    override fun getClassLCOM4ExceedThreshold(systemId: Long, threshold: Integer): List<ClassLCOM4> {
        val sql = "select c.system_id, c.name, c.module, m.lcom4 from JClass c \n" +
                "inner join class_metrics m on m.class_id = c.id \n" +
                "where c.system_id =:system_id and m.lcom4 >= :lcom4;"
        val classLCOM4POList = jdbi.withHandle<List<ClassLCOM4PO>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(SubModuleDubbo::class.java))
            it.createQuery(sql)
                    .bind("system_id", systemId)
                    .bind("lcom4", threshold)
                    .mapTo(ClassLCOM4PO::class.java)
                    .list()
        }
        return classLCOM4POList.map { it.toClassLCOM4() }
    }
}

class ClassLCOM4PO(val systemId: String, val className: String, val moduleName: String, val lcom4: Int) {
    fun toClassLCOM4(): ClassLCOM4 {
        return ClassLCOM4(JClassVO(className, moduleName), lcom4)
    }
}