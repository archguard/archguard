package com.thoughtworks.archguard.evaluation.infrastructure

import com.thoughtworks.archguard.evaluation.domain.EvaluationReport
import org.jdbi.v3.core.Jdbi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class EvaluationRepository(@Autowired private val jdbi: Jdbi) {

    fun save(evaluationReport: EvaluationReport): String {
        val uuid = UUID.randomUUID().toString()
        jdbi.withHandle<Int, Nothing> {
            it.createUpdate("insert into evaluationReport(id, name, dimensions, comment, improvements, createdDate) " +
                    "values ('${uuid}', '${evaluationReport.name}', '${evaluationReport.dimensions.joinToString(",")}'," +
                    " '${evaluationReport.comment}', '${evaluationReport.improvements.joinToString(",")}'," +
                    "'${evaluationReport.createdDate}')")
                    .execute()
        }
        return uuid
    }
}