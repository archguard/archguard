package com.thoghtworks.archguard.scan_jacoco

import com.thoghtworks.archguard.scan_jacoco.helper.Bean2Sql
import org.jacoco.core.analysis.Analyzer
import org.jacoco.core.analysis.CoverageBuilder
import org.jacoco.core.tools.ExecFileLoader
import java.io.File
import java.io.PrintWriter

class Service(val bean2Sql: Bean2Sql) {

    fun readJacoco(projectPath: String, bin: String, exec: String) {
        val builder = CoverageBuilder()
        val execFileLoader = ExecFileLoader()
        execFileLoader.load(File("$projectPath/$exec"))
        val analyzer = Analyzer(execFileLoader.executionDataStore, builder)
        analyzer.analyzeAll(File("$projectPath/$bin"))

        val bundle = builder.getBundle(projectPath.split("/").last())

        val coverage = Coverage(
                instructionCovered = bundle.instructionCounter.coveredCount,
                instructionMissed = bundle.instructionCounter.missedCount,
                lineCovered = bundle.lineCounter.coveredCount,
                lineMissed = bundle.lineCounter.missedCount,
                branchCovered = bundle.branchCounter.coveredCount,
                branchMissed = bundle.branchCounter.missedCount,
                complexityCovered = bundle.complexityCounter.coveredCount,
                complexityMissed = bundle.complexityCounter.missedCount,
                methodCovered = bundle.methodCounter.coveredCount,
                methodMissed = bundle.methodCounter.missedCount,
                classCovered = bundle.classCounter.coveredCount,
                classMissed = bundle.classCounter.missedCount,
                project = projectPath.split(File.separator).last(),
                scanTime = System.currentTimeMillis()
        )

        PrintWriter("output.sql").use {
            it.println(bean2Sql.bean2Sql(coverage))
        }
    }
}