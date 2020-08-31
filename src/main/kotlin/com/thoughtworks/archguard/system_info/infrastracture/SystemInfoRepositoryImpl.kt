package com.thoughtworks.archguard.system_info.infrastracture

import com.thoughtworks.archguard.system_info.domain.SystemInfo
import com.thoughtworks.archguard.system_info.domain.SystemInfoRepository
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
                it.createQuery("select id, system_name systemName, repo repo, sql_table `sql`," +
                        " username username, password password, repo_type repoType, scanned scanned, " +
                        "quality_gate_profile_id qualityGateProfileId from system_info where id = :id")
                        .bind("id", id)
                        .mapTo<SystemInfo>()
                        .firstOrNull()
            }

    override fun getSystemInfoList(): List<SystemInfo> =
            jdbi.withHandle<List<SystemInfo>, Nothing> {
                it.createQuery("select id, system_name systemName, repo repo, sql_table `sql`, username username, password password, scanned scanned, quality_gate_profile_id qualityGateProfileId,repo_type repoType from system_info")
                        .mapTo<SystemInfo>()
                        .list()
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
                    "quality_gate_profile_id = :qualityGateProfileId, " +
                    "updated_time = NOW() " +
                    "where id = :id")
                    .bindBean(systemInfo)
                    .execute()
        }
    }

    override fun addSystemInfo(systemInfo: SystemInfo): Long {
        return jdbi.withHandle<Long, Nothing> {
            it.createUpdate("insert into system_info" +
                    "(id, system_name, repo, sql_table, username, password, repo_type, quality_gate_profile_id, " +
                    " updated_time, created_time) " +
                    "values (:id, :systemName, " +
                    ":repo, " +
                    ":sql, " +
                    ":username, " +
                    ":password, " +
                    ":repoType, " +
                    ":qualityGateProfileId, " +
                    "NOW(), NOW())")
                    .bindBean(systemInfo)
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(Long::class.java)
                    .one()
        }
    }

    override fun queryBysystemName(systemName: String): Int =
            jdbi.withHandle<Int, Nothing> {
                it.createQuery("select count(*) from system_info where system_name = :systemName")
                        .bind("systemName", systemName)
                        .mapTo(Int::class.java)
                        .first()
            }

    override fun deleteSystemInfo(id: Long): Int {
        return jdbi.withHandle<Int, Nothing> {
            it.createUpdate("delete from system_info where id = :id")
                    .bind("id", id)
                    .execute()
        }
    }

}
