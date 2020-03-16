package com.thoughtworks.archguard.project_info.infrastracture

import com.thoughtworks.archguard.project_info.domain.dto.ProjectInfoDTO
import com.thoughtworks.archguard.project_info.domain.repository.ProjectInfoRepository
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

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
}