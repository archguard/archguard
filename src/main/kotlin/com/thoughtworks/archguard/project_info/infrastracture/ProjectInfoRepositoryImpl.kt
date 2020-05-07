package com.thoughtworks.archguard.project_info.infrastracture

import com.thoughtworks.archguard.project_info.domain.ProjectInfo
import com.thoughtworks.archguard.project_info.domain.ProjectInfoRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ProjectInfoRepositoryImpl : ProjectInfoRepository {

    @Autowired
    lateinit var jdbi: Jdbi
    override fun getProjectInfo(): ProjectInfo? =
            jdbi.withHandle<ProjectInfo, Nothing> {
                it
                        .createQuery("select id, name projectName, repo gitRepo, sql_table sql from ProjectInfo")
                        .map { rs, _ ->
                            ProjectInfo(rs.getString("id"),
                                    rs.getString("projectName"),
                                    rs.getString("gitRepo").split(','),
                                    rs.getString("sql"))
                        }
                        .firstOrNull()
            }

    override fun updateProjectInfo(projectInfo: ProjectInfo): Int =
            jdbi.withHandle<Int, Nothing> {
                it.createUpdate("update ProjectInfo set " +
                        "`name` = '${projectInfo.projectName}', " +
                        "repo = '${projectInfo.gitRepo.joinToString(",")}' " +
                        "sql_table = '${projectInfo.sql}'" +
                        "where id = '${projectInfo.id}'")
                        .execute()
            }

    override fun addProjectInfo(projectInfo: ProjectInfo): String {
        val uuid = UUID.randomUUID().toString()
        jdbi.withHandle<Int, Nothing> {
            it.createUpdate("insert into ProjectInfo(id, name, repo,sql_table, updatedAt, createdAt) values (" +
                    "'${uuid}', '${projectInfo.projectName}', " +
                    "'${projectInfo.gitRepo.joinToString(",")}', '${projectInfo.sql}', NOW(), NOW())")
                    .execute()
        }
        return uuid
    }

    override fun querySizeOfProjectInfo(): Int =
            jdbi.withHandle<Int, Nothing> {
                it.createQuery("select count(id) from ProjectInfo")
                        .mapTo(Int::class.java)
                        .first()
            }

}