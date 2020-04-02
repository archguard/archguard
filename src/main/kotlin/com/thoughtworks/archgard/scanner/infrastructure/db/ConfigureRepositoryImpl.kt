package com.thoughtworks.archgard.scanner.infrastructure.db

import com.thoughtworks.archgard.scanner.domain.config.dto.ConfigureDTO
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


    override fun getConfigures(): List<ConfigureDTO> =
            jdbi.withHandle<List<ConfigureDTO>, Nothing> { handle ->
                handle.registerRowMapper(ConstructorMapper.factory(ConfigureDTO::class.java))
                handle
                        .createQuery("select id, `type`, `key`, `value` from ScannerConfigure")
                        .mapTo(ConfigureDTO::class.java)
                        .list()
            }

    override fun updateConfigure(id: String, value: String): Int =
            jdbi.withHandle<Int, Nothing> { handle ->
                handle.createUpdate("update ScannerConfigure set `value` = :value, `updatedAt` = NOW() where `id` = :id")
                        .bind("id", id)
                        .bind("value", value)
                        .execute()
            }

    override fun register(scanners: List<String>) {
        configDao.saveAll(scanners.map {
            val typeAndKey = it.split('-')
            ConfigureDTO(UUID.randomUUID().toString(), typeAndKey[0], typeAndKey[1], "")
        })
    }

    override fun cleanRegistered(configs: List<String>) {
        configs.forEach {
            val typeAndKey = it.split('-')
            jdbi.withHandle<Int, Nothing> { handle ->
                handle.execute("DELETE FROM ScannerConfigure where `type` = ? and `key` = ?;", typeAndKey[0], typeAndKey[1])
            }
        }
    }

    override fun getRegistered(): List<String> =
            jdbi.withHandle<List<String>, Nothing> { handle ->
                handle
                        .createQuery("select `type` from ScannerConfigure where `key` = 'available' and `value` = 'true'")
                        .mapTo(String::class.java)
                        .list()
            }

    override fun ifAvailable(type: String): Boolean =
            jdbi.withHandle<Boolean, Nothing> { handle ->
                handle
                        .createQuery("select `value` from ScannerConfigure where `key` = 'available' and `type` = :type")
                        .bind("type", type)
                        .mapTo(Boolean::class.java)
                        .findFirst()
                        .orElse(false)
            }
}