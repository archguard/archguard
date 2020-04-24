package com.thoughtworks.archguard.evaluation.domain

import com.thoughtworks.archguard.evaluation.domain.analysis.*
import com.thoughtworks.archguard.evaluation.infrastructure.EvaluationRepository
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
    private val comment: String = "质量考虑，系统" +
            "在测试保护，数据库耦合，模块耦合方面做的不错，" +
            "在代码规范，变更影响方面阻碍较多，" +
            "在分层架构方面有待提升"
    private val improvements: List<String> = listOf("目前的分层架构检测出调用混乱，极易出现每层代码功能不单一")

    fun generateEvaluation(): String {
        val analysesReports = analyses.map { it.getQualityReport() }

        return evaluationRepository.save(EvaluationReport(null, LocalDateTime.now(), name,
                analyses.map { it.getName() },
                comment,
                analysesReports.flatMap { it.getImprovements() }))
    }
}
