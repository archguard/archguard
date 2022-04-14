package com.thoughtworks.archguard.scanner.infrastructure.db

import com.thoughtworks.archguard.scanner.domain.config.dto.ConfigureDTO
import com.thoughtworks.archguard.scanner.domain.config.model.ToolConfigure
import com.thoughtworks.archguard.scanner.domain.config.repository.ScannerConfigureRepository
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ScannerConfigureRepositoryImpl(@Autowired val configDao: ConfigDao) : ScannerConfigureRepository {

    @Autowired
    lateinit var jdbi: Jdbi

    override fun getConfigures(): List<ConfigureDTO> =
        jdbi.withHandle<List<ConfigureDTO>, Nothing> { handle ->
            handle.registerRowMapper(ConstructorMapper.factory(ConfigureDTO::class.java))
            handle
                .createQuery("select id, `type`, `key`, `value` from system_scanner_configure")
                .mapTo(ConfigureDTO::class.java)
                .list()
        }

    override fun getToolConfigures(): List<ToolConfigure> =
        getConfigures()
            .groupBy { it.type }
            .mapValues {
                it.value.map { i -> i.key to i.value }.toMap()
            }
            .map { ToolConfigure(it.key, it.value) }

    override fun updateConfigure(id: String, value: String): Int =
        jdbi.withHandle<Int, Nothing> { handle ->
            handle.createUpdate("update system_scanner_configure set `value` = :value, `updatedAt` = NOW() where `id` = :id")
                .bind("id", id)
                .bind("value", value)
                .execute()
        }

    override fun register(scanners: List<String>) {
        configDao.saveAll(
            scanners.map {
                val typeAndKey = it.split('-')
                ConfigureDTO(UUID.randomUUID().toString(), typeAndKey[0], typeAndKey[1], "")
            }
        )
    }

    override fun cleanRegistered(configs: List<String>) {
        configs.forEach {
            val typeAndKey = it.split('-')
            jdbi.withHandle<Int, Nothing> { handle ->
                handle.execute("DELETE FROM system_scanner_configure where `type` = ? and `key` = ?;", typeAndKey[0], typeAndKey[1])
            }
        }
    }

    override fun getRegistered(): List<String> =
        jdbi.withHandle<List<String>, Nothing> { handle ->
            handle
                .createQuery("select `type` from system_scanner_configure where `key` = 'available' and `value` = 'true'")
                .mapTo(String::class.java)
                .list()
        }

    override fun ifAvailable(type: String): Boolean =
        jdbi.withHandle<Boolean, Nothing> { handle ->
            handle
                .createQuery("select `value` from system_scanner_configure where `key` = 'available' and `type` = :type")
                .bind("type", type)
                .mapTo(Boolean::class.java)
                .findFirst()
                .orElse(false)
        }
}
