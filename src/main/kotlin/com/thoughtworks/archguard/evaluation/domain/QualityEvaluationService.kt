package com.thoughtworks.archguard.evaluation.domain

import com.thoughtworks.archguard.evaluation.infrastructure.EvaluationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class QualityEvaluationService(@Autowired val evaluationRepository: EvaluationRepository) {

    private val name: String = "质量评估"
    private val dimensions: List<String> = listOf("测试保护", "数据库耦合", "模块耦合", "分层架构", "代码规范", "变更影响")
    private val comment: String = "质量考虑，系统在测试保护，数据库耦合，模块耦合方面做的不错，但是在代码规范，变更影响方面阻碍较多，" +
            "分层架构方面有待提升"
    private val improvements: List<String> = listOf("目前的分层架构检测出调用混乱，极易出现每层代码功能不单一")
    fun generateEvaluation(): String {
        return evaluationRepository.save(EvaluationReport(null, LocalDateTime.now(), name, dimensions, comment, improvements))
    }
}
