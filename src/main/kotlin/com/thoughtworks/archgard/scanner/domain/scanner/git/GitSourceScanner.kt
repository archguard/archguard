package com.thoughtworks.archgard.scanner.domain.scanner.git

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.config.model.ToolConfigure
import com.thoughtworks.archgard.scanner.domain.scanner.Scanner
import com.thoughtworks.archgard.scanner.domain.tools.GitScannerTool
import com.thoughtworks.archgard.scanner.infrastructure.db.SqlScriptRunner
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class GitSourceScanner(@Autowired val sqlScriptRunner: SqlScriptRunner) : Scanner {
    private val DELETE_GIT_REPO = "delete from git_rep where 1=1"
    private val DELETE_COMMIT_LOG = "delete from commit_log where 1=1"
    private val DELETE_CHANGE_ENTRY = "delete from change_entry where 1=1"

    private val log = LoggerFactory.getLogger(GitSourceScanner::class.java)
    override fun getScannerName(): String {
        return "GitSource"
    }

    override fun toolListGenerator(): List<ToolConfigure> {
        val result = ArrayList<ToolConfigure>()
        val config = HashMap<String, String>()
        config["available"] = "false"
        result.add(ToolConfigure(getScannerName(), config))
        return result
    }

    override fun scan(context: ScanContext) {
        log.info("start scan git source")
        val gitScannerTool = GitScannerTool(context.workspace, "master")
        val gitReport = gitScannerTool.getGitReport()
        if (gitReport != null) {
            sqlScriptRunner.run(DELETE_GIT_REPO)
            sqlScriptRunner.run(DELETE_COMMIT_LOG)
            sqlScriptRunner.run(DELETE_CHANGE_ENTRY)
            sqlScriptRunner.run(gitReport)
        }
        log.info("finished scan git source")
    }

}