package com.thoughtworks.archguard.project_info.infrastracture

import com.thoughtworks.archguard.project_info.domain.ProjectInfo
import com.thoughtworks.archguard.project_info.domain.ProjectInfoRepository
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.mapTo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class ProjectInfoRepositoryImpl : ProjectInfoRepository {

    @Autowired
    lateinit var jdbi: Jdbi
    override fun getProjectInfo(id: Long): ProjectInfo? =
            jdbi.withHandle<ProjectInfo, Nothing> {
                it.createQuery("select id, project_name projectName, repo repo, sql_table `sql`," +
                        " username username, password password, repo_type repoType from project_info where id = :id")
                        .bind("id", id)
                        .mapTo<ProjectInfo>()
                        .firstOrNull()
            }

    override fun getProjectInfoList(): List<ProjectInfo> =
            jdbi.withHandle<List<ProjectInfo>, Nothing> {
                it.createQuery("select id, project_name projectName, repo repo, sql_table `sql`, username username, password password, repo_type repoType from project_info")
                        .mapTo<ProjectInfo>()
                        .list()
            }

    override fun updateProjectInfo(projectInfo: ProjectInfo): Int {
        return jdbi.withHandle<Int, Nothing> {
            it.createUpdate("update project_info set " +
                    "project_name = :projectName, " +
                    "repo = :repo, " +
                    "sql_table = :sql, " +
                    "username = :username, " +
                    "password = :password, " +
                    "repo_type = :repoType, " +
                    "updated_time = NOW() " +
                    "where id = :id")
                    .bindBean(projectInfo)
                    .execute()
        }
    }

    override fun addProjectInfo(projectInfo: ProjectInfo): Long {
        return jdbi.withHandle<Long, Nothing> {
            it.createUpdate("insert into project_info(id, project_name, repo, sql_table, username, password, repo_type, updated_time, created_time) " +
                    "values (:id, :projectName, " +
                    ":repo, " +
                    ":sql, " +
                    ":username, " +
                    ":password, " +
                    ":repoType, " +
                    "NOW(), NOW())")
                    .bindBean(projectInfo)
                    .executeAndReturnGeneratedKeys("id")
                    .mapTo(Long::class.java)
                    .one()
        }
    }

    override fun queryByProjectName(projectName: String): Int =
            jdbi.withHandle<Int, Nothing> {
                it.createQuery("select count(*) from project_info where project_name = :projectName")
                        .bind("projectName", projectName)
                        .mapTo(Int::class.java)
                        .first()
            }

    override fun deleteProjectInfo(id: Long): Int {
        return jdbi.withHandle<Int, Nothing> {
            it.createUpdate("delete from project_info where id = :id")
                    .bind("id", id)
                    .execute()
        }
    }

}
