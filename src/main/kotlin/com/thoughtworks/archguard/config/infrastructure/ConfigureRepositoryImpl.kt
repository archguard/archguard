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

    override fun getConfigures(projectId: Long): List<Configure> {
        val sql = "SELECT id, `project_id`, type, `key`, value, `order` FROM Configure where `project_id` = :projectId"
        return jdbi.withHandle<List<Configure>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(ConfigureDTO::class.java))
            it.createQuery(sql)
                    .bind("projectId", projectId)
                    .mapTo(ConfigureDTO::class.java)
                    .map { configure -> configure.toDomainObject() }
                    .list()
        }
    }

    override fun batchCreateConfigures(configs: List<Configure>) {
        val configureDTOList = configs.map { ConfigureDTO.of(it) }.toList()
        jdbi.withHandle<IntArray, Nothing> { handle ->
            val sql = "INSERT INTO Configure (id, project_id, type, `key`, value, `order`) VALUES (:id, :projectId, :type, :key, :value, :order)"
            val batch = handle.prepareBatch(sql)
            configureDTOList.forEach {
                val id = it.id ?: UUID.randomUUID().toString()
                batch.bind("id", id)
                        .bind("project_id", it.projectId)
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
        val configureDTO = ConfigureDTO.of(config)
        jdbi.withHandle<Int, Nothing> { handle ->
            handle.execute("INSERT INTO Configure (id, project_id, type, `key`, value, `order`) VALUES (?, ?, ?, ?, ?, ?)",
                    configureDTO.id, configureDTO.projectId, configureDTO.type, configureDTO.key, configureDTO.value, configureDTO.order)
        }
    }

    override fun update(config: Configure) {
        val configureDTO = ConfigureDTO.of(config)
        val sql = "update Configure set " +
                "type='${configureDTO.type}', "
        "key='${configureDTO.key}', "
        "value='${configureDTO.value}', "
        "order='${configureDTO.order}', "
        "where id='${configureDTO.id}'"
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

    override fun getConfiguresByType(projectId: Long, type: String): List<Configure> {
        val sql = "SELECT id, type, `key`, value, `order` FROM Configure WHERE type=:type and project_id = :projectId"
        return jdbi.withHandle<List<Configure>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(ConfigureDTO::class.java))
            it.createQuery(sql)
                    .bind("type", type)
                    .bind("projectId", projectId)
                    .mapTo(ConfigureDTO::class.java)
                    .map { configure -> configure.toDomainObject() }
                    .list()
        }
    }

    override fun deleteConfiguresByType(projectId: Long, type: String) {
        val sql = "delete from Configure where type = '$type' and project_id = $projectId"
        jdbi.withHandle<Int, Nothing> { handle ->
            handle.execute(sql)
        }
    }
}
