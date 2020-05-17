package com.thoughtworks.archgard.scanner.domain.analyser

import com.thoughtworks.archgard.scanner.domain.project.ProjectRepository
import com.thoughtworks.archgard.scanner.domain.tools.InvokeSqlTool
import com.thoughtworks.archgard.scanner.infrastructure.FileOperator.deleteDirectory
import com.thoughtworks.archgard.scanner.infrastructure.db.SqlScriptRunner
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.File

@Service
class SqlDependencyAnalysis(@Autowired val projectRepository: ProjectRepository, @Autowired val sqlScriptRunner: SqlScriptRunner) {

    private val log = LoggerFactory.getLogger(SqlDependencyAnalysis::class.java)

    private val DELETE_PROCEDURE = "delete from PLProcedure where 1=1"
    private val DELETE_PROCEDURE_CALLEE = "delete from _PLProcedureCallees where 1=1"
    private val DELETE_ACTION = "delete from _PLProcedureSqlAction where 1=1"
    fun analyse() {
        log.info("start scan sql analysis")
        val project = projectRepository.getProjectInfo().getSource()
        val git = File(project.workspace.path + "/.git")
        deleteDirectory(git)
        val invokeSqlTool = InvokeSqlTool(project.workspace)
        val analyseFile = invokeSqlTool.analyse()
        if (analyseFile.find { !it.exists() } == null) {
            sqlScriptRunner.run(DELETE_ACTION)
            sqlScriptRunner.run(DELETE_PROCEDURE_CALLEE)
            sqlScriptRunner.run(DELETE_PROCEDURE)
            sqlScriptRunner.run(analyseFile)
        }
        log.info("finished scan sql analysis")
    }

}
