package com.thoughtworks.archgard.hub.infrastracture

import com.thoughtworks.archgard.hub.domain.model.ProjectInfo
import com.thoughtworks.archgard.hub.domain.repository.HubRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class HubRepositoryImpl : HubRepository {

    @Autowired
    lateinit var jdbi: Jdbi

    override fun getProjectInfo(): ProjectInfo =
            jdbi.withHandle<ProjectInfo, Nothing> {
                it
                        .createQuery("select id, name projectName, repo gitRepo from ProjectInfo")
                        .mapTo(ProjectInfo::class.java)
                        .first()
            }
}