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
        val sql = "SELECT id, type, `key`, value, `order` FROM Configure"
        return jdbi.withHandle<List<NodeConfigure>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(NodeConfigure::class.java))
            it.createQuery(sql)
                    .mapTo(NodeConfigure::class.java)
                    .list()
        }
    }

    override fun create(config: NodeConfigure) {
        jdbi.withHandle<Int, Nothing> { handle ->
            handle.execute("INSERT INTO Configure (id, type, `key`, value, `order`) VALUES (?, ?, ?, ?, ?)",
                    config.id, config.type, config.key, config.value, config.order)
        }
    }

    override fun update(config: NodeConfigure) {
        val sql = "update Configure set " +
                "type='${config.type}', "
                "key='${config.key}', "
                "value='${config.value}', "
                "order='${config.order}', "
                "where id='${config.id}'"
        jdbi.withHandle<Int, Nothing> { handle ->
            handle.createUpdate(sql).execute()
        }
    }

    override fun delete(id: String) {
        val sql = "delete from Configure where id = '$id'"
        jdbi.withHandle<Int, Nothing> {handle ->
            handle.execute(sql)
        }
    }
}