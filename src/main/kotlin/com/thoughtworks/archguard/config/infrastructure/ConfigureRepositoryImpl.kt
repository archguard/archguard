package com.thoughtworks.archguard.config.infrastructure

import com.thoughtworks.archguard.config.domain.ConfigureRepository
import com.thoughtworks.archguard.config.domain.NodeConfigure
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class ConfigureRepositoryImpl : ConfigureRepository {
    @Autowired
    lateinit var jdbi: Jdbi

    override fun getConfigures(): List<NodeConfigure> {
        val sql = "select id, `type`, `key`, value, `order` from Configure"
        return jdbi.withHandle<List<NodeConfigure>, Nothing>{
            it.registerRowMapper(ConstructorMapper.factory(NodeConfigure::class.java))
            it.createQuery(sql)
                    .mapTo(NodeConfigure::class.java)
                    .list()
        }
    }
}