package com.thoughtworks.archguard.config.infrastructure

import com.thoughtworks.archguard.config.domain.ConfigureRepository
import com.thoughtworks.archguard.config.domain.Configure
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ConfigureRepositoryImpl : ConfigureRepository {
    @Autowired
    lateinit var jdbi: Jdbi

    override fun getConfigures(): List<Configure> {
        val sql = "SELECT id, type, `key`, value, `order` FROM Configure"
        return jdbi.withHandle<List<Configure>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(Configure::class.java))
            it.createQuery(sql)
                    .mapTo(Configure::class.java)
                    .list()
        }
    }

    override fun batchCreateConfigures(configs: List<Configure>) {
        jdbi.withHandle<IntArray, Nothing> { handle ->
            val sql = "INSERT INTO Configure (id, type, `key`, value, `order`) VALUES (:id, :type, :key, :value, :order)"
            val batch = handle.prepareBatch(sql)
            configs.forEach {
                val id = it.id ?: UUID.randomUUID().toString()
                batch.bind("id", id)
                        .bind("type", it.type)
                        .bind("key", it.key)
                        .bind("value", it.value)
                        .bind("order", it.order)
                        .add()
            }
            batch.execute()
        }
    }

    override fun create(config: Configure) {
        jdbi.withHandle<Int, Nothing> { handle ->
            handle.execute("INSERT INTO Configure (id, type, `key`, value, `order`) VALUES (?, ?, ?, ?, ?)",
                    config.id, config.type, config.key, config.value, config.order)
        }
    }

    override fun update(config: Configure) {
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
        jdbi.withHandle<Int, Nothing> { handle ->
            handle.execute(sql)
        }
    }

    override fun getConfiguresByType(type: String): List<Configure> {
        val sql = "SELECT id, type, `key`, value, `order` FROM Configure WHERE type='$type'"
        return jdbi.withHandle<List<Configure>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(Configure::class.java))
            it.createQuery(sql)
                    .mapTo(Configure::class.java)
                    .list()
        }
    }

    override fun deleteConfiguresByType(type: String) {
        val sql = "delete from Configure where type = '$type'"
        jdbi.withHandle<Int, Nothing> { handle ->
            handle.execute(sql)
        }
    }
}
