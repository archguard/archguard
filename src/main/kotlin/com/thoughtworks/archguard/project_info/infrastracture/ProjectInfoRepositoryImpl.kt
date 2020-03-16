package com.thoughtworks.archguard.project_info.infrastracture

import com.thoughtworks.archguard.project_info.domain.dto.ProjectInfoAddDTO
import com.thoughtworks.archguard.project_info.domain.dto.ProjectInfoDTO
import com.thoughtworks.archguard.project_info.domain.repository.ProjectInfoRepository
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ProjectInfoRepositoryImpl : ProjectInfoRepository {

    @Autowired
    lateinit var jdbi: Jdbi
    override fun getProjectInfo(): ProjectInfoDTO =
            jdbi.withHandle<ProjectInfoDTO, Nothing> {
                it.registerRowMapper(ConstructorMapper.factory(ProjectInfoDTO::class.java))
                        .createQuery("select id, name projectName, repo gitRepo from ProjectInfo")
                        .mapTo(ProjectInfoDTO::class.java)
                        .one()
            }

    override fun updateProjectInfo(projectInfo: ProjectInfoDTO): Int =
            jdbi.withHandle<Int, Nothing> {
                it.createUpdate("update ProjectInfo set `name` = '${projectInfo.projectName}', repo = '${projectInfo.gitRepo}' where id = '${projectInfo.id}'")
                        .execute()
            }

    override fun addProjectInfo(projectInfo: ProjectInfoAddDTO): String {
        val uuid = UUID.randomUUID().toString()
        jdbi.withHandle<Int, Nothing> {
            it.createUpdate("insert into ProjectInfo(id, name, repo, updatedAt, createdAt) values ('${uuid}', '${projectInfo.projectName}', '${projectInfo.gitRepo}', NOW(), NOW())")
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