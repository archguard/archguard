package com.thoughtworks.archgard.scanner.infrastructure.db

import com.thoughtworks.archgard.scanner.domain.system.SystemInfo
import com.thoughtworks.archgard.scanner.domain.system.SystemInfoRepository
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.mapTo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class SystemInfoRepositoryImpl : SystemInfoRepository {

    @Autowired
    lateinit var jdbi: Jdbi

    override fun getSystemInfo(id: Long): SystemInfo? =
            jdbi.withHandle<SystemInfo, Nothing> {
                it.createQuery("select id, project_name systemName, repo repo, sql_table `sql`," +
                        " username username, password password, repo_type repoType from project_info where id = :id")
                        .bind("id", id)
                        .mapTo<SystemInfo>()
                        .firstOrNull()
            }
}
