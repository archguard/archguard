package com.thoughtworks.archgard.scanner.infrastructure.db

import com.thoughtworks.archgard.scanner.domain.project.Project
import com.thoughtworks.archgard.scanner.domain.project.ProjectRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class ProjectRepositoryImpl : ProjectRepository {

    @Autowired
    lateinit var jdbi: Jdbi

    override fun getProjectInfo(): Project =
            jdbi.withHandle<Project, Nothing> {
                it
                        .createQuery("select id, name projectName, repo gitRepo, sql_table from ProjectInfo")
                        .mapTo(Project::class.java)
                        .first()
            }
}