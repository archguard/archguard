package com.thoughtworks.archgard.scanner.infrastructure.db

import com.thoughtworks.archgard.scanner.domain.config.dto.ConfigureDTO
import com.thoughtworks.archgard.scanner.domain.config.model.ScannerConfigure
import com.thoughtworks.archgard.scanner.domain.config.repository.ConfigureRepository
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*


@Repository
class ConfigureRepositoryImpl(@Autowired val configDao: ConfigDao) : ConfigureRepository {

    @Autowired
    lateinit var jdbi: Jdbi

    override fun addConfigure(config: ConfigureDTO): String {
        val uuid = UUID.randomUUID().toString()
        jdbi.withHandle<Int, Nothing> { handle ->
            handle.registerRowMapper(ConstructorMapper.factory(ConfigureDTO::class.java))
            handle.createUpdate("INSERT INTO ScannerConfigure (`id`, `type`, `key`, `value`, `updatedAt`, `createdAt`) VALUES (:uuid, :type, :key, :value, now(), now(), :order);")
                    .bind("type", config.type)
                    .bind("key", config.key)
                    .bind("value", config.value)
                    .bind("uuid", uuid)
                    .execute()
        }
        return uuid
    }

    override fun getConfigures(): List<ScannerConfigure> =
            jdbi.withHandle<List<ScannerConfigure>, Nothing> { handle ->
                handle.registerRowMapper(ConstructorMapper.factory(ScannerConfigure::class.java))
                handle
                        .createQuery("select id, `type`, `key`, `value` from ScannerConfigure")
                        .mapTo(ScannerConfigure::class.java)
                        .list()
            }

    override fun updateConfigure(id: String, type: String?, key: String?, value: String?): Int =
            jdbi.withHandle<Int, Nothing> { handle ->
                handle.createUpdate("update ScannerConfigure set `type` = :type, `key` = :key, `value` = :value, `updatedAt` = NOW() where id = :id")
                        .bind("type", type)
                        .bind("key", key)
                        .bind("value", value)
                        .bind("id", id)
                        .execute()
            }

    override fun register(scanners: List<String>) {
        configDao.saveAll(scanners.map { ScannerConfigure(UUID.randomUUID().toString(), it, "available", "false") })
    }

    override fun getRegistered(): List<ScannerConfigure> =
            jdbi.withHandle<List<ScannerConfigure>, Nothing> { handle ->
                handle.registerRowMapper(ConstructorMapper.factory(ScannerConfigure::class.java))
                handle
                        .createQuery("select id, `type`, `key`, `value` from ScannerConfigure where `key` = 'available'")
                        .mapTo(ScannerConfigure::class.java)
                        .list()
            }

    override fun cleanRegistered(configs: List<String>) {
        configs.forEach {
            jdbi.withHandle<Int, Nothing> { handle ->
                handle.execute("DELETE FROM ScannerConfigure where `type` = ?;", it)
            }
        }
    }
}