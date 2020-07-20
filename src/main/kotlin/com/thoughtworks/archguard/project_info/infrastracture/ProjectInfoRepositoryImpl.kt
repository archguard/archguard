package com.thoughtworks.archguard.project_info.infrastracture

import com.thoughtworks.archguard.project_info.domain.ProjectInfo
import com.thoughtworks.archguard.project_info.domain.ProjectInfoRepository
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.mapTo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ProjectInfoRepositoryImpl : ProjectInfoRepository {

    @Autowired
    lateinit var jdbi: Jdbi
    override fun getProjectInfo(): ProjectInfo? =
            jdbi.withHandle<ProjectInfo, Nothing> {
                it.createQuery("select id, name projectName, repo repo, sql_table `sql`, username username, password password, repo_type repoType from ProjectInfo")
                        .mapTo<ProjectInfo>()
                        .firstOrNull()
            }

    override fun updateProjectInfo(projectInfo: ProjectInfo): Int {
        return jdbi.withHandle<Int, Nothing> {
            it.createUpdate("update ProjectInfo set " +
                    "`name` = :projectName, " +
                    "repo = :repo, " +
                    "sql_table = :sql, " +
                    "username = :username, " +
                    "password = :password, " +
                    "repo_type = :repoType " +
                    "where id = :id")
                    .bindBean(projectInfo)
                    .execute()
        }
    }

    override fun addProjectInfo(projectInfo: ProjectInfo): String {
        projectInfo.id = UUID.randomUUID().toString()
        jdbi.withHandle<Int, Nothing> {
            it.createUpdate("insert into ProjectInfo(id, name, repo, sql_table, username, password, repo_type, updatedAt, createdAt) " +
                    "values (:id, :projectName, " +
                    ":repo, " +
                    ":sql, " +
                    ":username, " +
                    ":password, " +
                    ":repoType, " +
                    "NOW(), NOW())")
                    .bindBean(projectInfo)
                    .execute()
        }
        return projectInfo.id
    }

    override fun querySizeOfProjectInfo(): Int =
            jdbi.withHandle<Int, Nothing> {
                it.createQuery("select count(id) from ProjectInfo")
                        .mapTo(Int::class.java)
                        .first()
            }

}
