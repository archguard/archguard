package com.thoughtworks.archgard.scanner.domain.analyser

import com.thoughtworks.archgard.scanner.domain.project.ProjectRepository
import com.thoughtworks.archgard.scanner.domain.tools.InvokeSqlTool
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.File

@Service
class SqlDependencyAnalysis(@Autowired val projectRepository: ProjectRepository) {

    private val log = LoggerFactory.getLogger(SqlDependencyAnalysis::class.java)

    fun analyse() {
        log.info("start scan sql analysis")
        val project = projectRepository.getProjectInfo().getSource()
        val git = File(project.workspace.path + "/.git")
        git.deleteOnExit()
        val invokeSqlTool = InvokeSqlTool(project.workspace)
        invokeSqlTool.analyse()
        log.info("finished scan sql analysis")

    }

}
