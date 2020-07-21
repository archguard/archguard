package com.thoughtworks.archgard.scanner.infrastructure.db

import com.thoughtworks.archgard.scanner.domain.project.ProjectInfo
import com.thoughtworks.archgard.scanner.domain.project.ProjectRepository
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.mapTo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class ProjectRepositoryImpl : ProjectRepository {

    @Autowired
    lateinit var jdbi: Jdbi

    override fun getProjectInfo(): ProjectInfo =
            jdbi.withHandle<ProjectInfo, Nothing> {
                it.createQuery("""
                    select id, name projectName, repo repo, sql_table `sql`, 
                    username username, password password, 
                    repo_type repoType from ProjectInfo
                """).mapTo<ProjectInfo>().first()
            }
}
