package com.thoughtworks.archgard.scanner.infrastructure.db

import com.thoughtworks.archgard.scanner.domain.config.dto.ConfigureDTO
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.SqlBatch


interface ConfigDao {

    @SqlBatch("insert into ScannerConfigure (`id`, `type`, `key`, `value`, `updatedAt`, `createdAt`) " +
            "values (:config.id, :config.type, :config.key, :config.value, NOW(), NOW())")
    fun saveAll(@BindBean("config") configList: List<ConfigureDTO>)

}