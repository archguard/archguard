package com.thoughtworks.archguard.scanner.infrastructure.db

import com.thoughtworks.archguard.scanner.domain.system.SystemInfo
import com.thoughtworks.archguard.scanner.domain.system.SystemInfoRepository
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.mapTo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class ScannerSystemInfoRepositoryImpl : SystemInfoRepository {

    @Autowired
    lateinit var jdbi: Jdbi

    override fun getSystemInfo(id: Long): SystemInfo? =
            jdbi.withHandle<SystemInfo, Nothing> {
                it.createQuery("select id, system_name systemName, repo repo, sql_table `sql`," +
                        " username username, language language, branch branch, code_path codePath," +
                        " password password, repo_type repoType from system_info where id = :id")
                        .bind("id", id)
                        .mapTo<SystemInfo>()
                        .firstOrNull()
            }

    override fun updateSystemInfo(systemInfo: SystemInfo): Int {
        return jdbi.withHandle<Int, Nothing> {
            it.createUpdate("update system_info set " +
                    "system_name = :systemName, " +
                    "repo = :repo, " +
                    "sql_table = :sql, " +
                    "username = :username, " +
                    "password = :password, " +
                    "repo_type = :repoType, " +
                    "scanned = :scanned, " +
                    "language = :language, " +
                    "code_path = :codePath, " +
                    "branch = :branch, " +
                    "updated_time = NOW() " +
                    "where id = :id")
                    .bindBean(systemInfo)
                    .execute()
        }
    }

    override fun updateScanningSystemToScanFail() {
        jdbi.withHandle<Unit, Nothing> {
            it.createUpdate("update system_info s1 set s1.scanned='FAILED' where s1.id in " +
                    "(select s2.id from (select * from system_info) s2 where s2.scanned='SCANNING') ;")
                    .execute()
        }
    }
}
