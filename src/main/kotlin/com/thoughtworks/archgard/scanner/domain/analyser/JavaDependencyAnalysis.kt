package com.thoughtworks.archgard.scanner.domain.analyser

import com.thoughtworks.archgard.scanner.domain.project.ProjectRepository
import com.thoughtworks.archgard.scanner.domain.tools.JavaByteCodeTool
import com.thoughtworks.archgard.scanner.domain.tools.TableUsedTool
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class JavaDependencyAnalysis(@Value("\${spring.datasource.url}") val dbUrl: String,
                             @Value("\${spring.datasource.username}") val username: String,
                             @Value("\${spring.datasource.password}") val password: String,
                             @Autowired val projectRepository: ProjectRepository) {
    private val log = LoggerFactory.getLogger(JavaDependencyAnalysis::class.java)

    fun analyse() {
        log.info("start scan java analysis")
        val build = projectRepository.getProjectInfo().build()
        val url = dbUrl.replace("://", "://" + username + ":" + password + "@")
        val javaByteCodeTool = JavaByteCodeTool(build.workspace, url)
        javaByteCodeTool.analyse()
        val tableUsedTool = TableUsedTool(build.workspace, build.sql)
        tableUsedTool.analyse()
        log.info("finished scan java analysis")
    }
}