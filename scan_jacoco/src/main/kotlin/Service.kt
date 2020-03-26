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

        val bundleCoverage = builder.getBundle(projectPath.split("/").last())

        PrintWriter("output.sql").use { writer ->

            val bundle = Bundle(
                    instructionCovered = bundleCoverage.instructionCounter.coveredCount,
                    instructionMissed = bundleCoverage.instructionCounter.missedCount,
                    lineCovered = bundleCoverage.lineCounter.coveredCount,
                    lineMissed = bundleCoverage.lineCounter.missedCount,
                    branchCovered = bundleCoverage.branchCounter.coveredCount,
                    branchMissed = bundleCoverage.branchCounter.missedCount,
                    complexityCovered = bundleCoverage.complexityCounter.coveredCount,
                    complexityMissed = bundleCoverage.complexityCounter.missedCount,
                    methodCovered = bundleCoverage.methodCounter.coveredCount,
                    methodMissed = bundleCoverage.methodCounter.missedCount,
                    classCovered = bundleCoverage.classCounter.coveredCount,
                    classMissed = bundleCoverage.classCounter.missedCount,
                    bundleName = projectPath.split(File.separator).last(),
                    scanTime = System.currentTimeMillis()
            )
            writer.println(bean2Sql.bean2Sql(bundle))

            bundleCoverage.packages.forEach { pcg ->
                val packageItem = Item(
                        instructionCovered = pcg.instructionCounter.coveredCount,
                        instructionMissed = pcg.instructionCounter.missedCount,
                        lineCovered = pcg.lineCounter.coveredCount,
                        lineMissed = pcg.lineCounter.missedCount,
                        branchCovered = pcg.branchCounter.coveredCount,
                        branchMissed = pcg.branchCounter.missedCount,
                        complexityCovered = pcg.complexityCounter.coveredCount,
                        complexityMissed = pcg.complexityCounter.missedCount,
                        methodCovered = pcg.methodCounter.coveredCount,
                        methodMissed = pcg.methodCounter.missedCount,
                        classCovered = pcg.classCounter.coveredCount,
                        classMissed = pcg.classCounter.missedCount,
                        itemName = pcg.name,
                        itemType = ItemType.PACKAGE,
                        bundleName = bundle.bundleName,
                        scanTime = bundle.scanTime
                )
                writer.println(bean2Sql.bean2Sql(packageItem))

                pcg.sourceFiles.forEach { file ->
                    val fileItem = Item(
                            instructionCovered = file.instructionCounter.coveredCount,
                            instructionMissed = file.instructionCounter.missedCount,
                            lineCovered = file.lineCounter.coveredCount,
                            lineMissed = file.lineCounter.missedCount,
                            branchCovered = file.branchCounter.coveredCount,
                            branchMissed = file.branchCounter.missedCount,
                            complexityCovered = file.complexityCounter.coveredCount,
                            complexityMissed = file.complexityCounter.missedCount,
                            methodCovered = file.methodCounter.coveredCount,
                            methodMissed = file.methodCounter.missedCount,
                            classCovered = file.classCounter.coveredCount,
                            classMissed = file.classCounter.missedCount,
                            itemName = file.packageName + "/" + file.name,
                            itemType = ItemType.FILE,
                            bundleName = bundle.bundleName,
                            scanTime = bundle.scanTime
                    )

                    writer.println(bean2Sql.bean2Sql(fileItem))

                }
            }

        }
    }
}