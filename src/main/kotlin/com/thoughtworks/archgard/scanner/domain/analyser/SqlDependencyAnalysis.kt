package com.thoughtworks.archgard.scanner.domain.analyser

import com.thoughtworks.archgard.scanner.domain.project.CompiledProject
import com.thoughtworks.archgard.scanner.domain.tools.InvokeSqlTool
import com.thoughtworks.archgard.scanner.infrastructure.FileOperator.deleteDirectory
import com.thoughtworks.archgard.scanner.infrastructure.db.SqlScriptRunner
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.File

@Service
class SqlDependencyAnalysis(@Autowired val analysisService: AnalysisService,
                            @Autowired val sqlScriptRunner: SqlScriptRunner) {

    private val log = LoggerFactory.getLogger(SqlDependencyAnalysis::class.java)

    private val DELETE_PROCEDURE = "delete from PLProcedure where 1=1"
    private val DELETE_PROCEDURE_CALLEE = "delete from _PLProcedureCallees where 1=1"
    private val DELETE_ACTION = "delete from _PLProcedureSqlAction where 1=1"
    fun analyse(id: Long) {
        log.info("start scan sql analysis")
        val projectOperator = analysisService.getProjectOperator(id)
        projectOperator.cloneAllRepo()
        projectOperator.compiledProjectMap.forEach { (_, compiledProject) ->
            analysisSingleCompliedProject(compiledProject)
        }

        log.info("finished scan sql analysis")
    }

    private fun analysisSingleCompliedProject(compiledProject: CompiledProject) {
        val git = File(compiledProject.workspace.path + "/.git")
        deleteDirectory(git)
        val invokeSqlTool = InvokeSqlTool(compiledProject.workspace)
        val analyseFile = invokeSqlTool.analyse()
        if (analyseFile.find { !it.exists() } == null) {
            sqlScriptRunner.run(DELETE_ACTION)
            sqlScriptRunner.run(DELETE_PROCEDURE_CALLEE)
            sqlScriptRunner.run(DELETE_PROCEDURE)
            sqlScriptRunner.run(analyseFile)
        }
    }

}
