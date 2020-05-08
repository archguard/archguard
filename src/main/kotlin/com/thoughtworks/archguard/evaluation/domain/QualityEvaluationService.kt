package com.thoughtworks.archguard.evaluation.domain

import com.thoughtworks.archguard.evaluation.domain.analysis.*
import com.thoughtworks.archguard.evaluation.domain.analysis.report.Report
import com.thoughtworks.archguard.evaluation.domain.analysis.report.ReportLevel
import com.thoughtworks.archguard.evaluation.infrastructure.EvaluationRepository
import org.jetbrains.kotlin.utils.keysToMap
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class QualityEvaluationService(@Autowired val evaluationRepository: EvaluationRepository,
                               @Autowired val testProtectionAnalysis: TestProtectionAnalysis,
                               @Autowired val dbCouplingAnalysis: DbCouplingAnalysis,
                               @Autowired val moduleCouplingAnalysis: ModuleCouplingAnalysis,
                               @Autowired val layerAnalysis: LayerAnalysis,
                               @Autowired val codeStyleAnalysis: CodeStyleAnalysis,
                               @Autowired val changeImpactAnalysis: ChangeImpactAnalysis) {

    private val name: String = "质量评估"
    private val analyses: List<Analysis> = listOf(testProtectionAnalysis, dbCouplingAnalysis, moduleCouplingAnalysis,
            layerAnalysis, codeStyleAnalysis, changeImpactAnalysis)


    fun generateEvaluation(): String {
        val analysesReports = analyses.keysToMap { it.getQualityReport() }.filterValues { it != null }

        return evaluationRepository.save(EvaluationReport(null, LocalDateTime.now(), name,
                analysesReports.map { Dimension(it.key.getName(), it.value!!.getLevel()) },
                getComment(analysesReports),
                analysesReports.values.flatMap { it!!.getImprovements() }),
                EvaluationReportDetail(analysesReports.values.map { it!!.getReportDetail() }))
    }

    private fun getComment(analysesReports: Map<Analysis, Report?>): String {
        val commentTemplate: String = "质量考虑，系统" +
                "%s方面做的不错，" +
                "%s方面有待提升"
        val goodComment = analysesReports.filterValues { it!!.getComment() == ReportLevel.GOOD }.map { it.key.getName() }.joinToString(",")
        val improvedComment = analysesReports.filterValues { it!!.getComment() == ReportLevel.NEED_IMPROVED }.map { it.key.getName() }.joinToString(",")
        return String.format(commentTemplate, goodComment, improvedComment)
    }
}
