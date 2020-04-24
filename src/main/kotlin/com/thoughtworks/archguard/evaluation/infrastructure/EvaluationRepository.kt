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

    fun findAll(): List<EvaluationReport> {
        return jdbi.withHandle<List<EvaluationReport>, Nothing> {
            it
                    .createQuery("select id, name, dimensions, comment, improvements, createdDate from evaluationReport order by createdDate desc")
                    .map { rs, _ ->
                        EvaluationReport(rs.getString("id"),
                                rs.getTimestamp("createdDate").toLocalDateTime(),
                                rs.getString("name"),
                                rs.getString("dimensions").split(","),
                                rs.getString("comment"),
                                rs.getString("improvements").split(","))
                    }.list()
        }

    }

    fun findById(id: String): EvaluationReport? {
        return jdbi.withHandle<EvaluationReport?, Nothing> {
            it
                    .createQuery("select id, name, dimensions, comment, improvements, createdDate from evaluationReport where id='${id}'")
                    .map { rs, _ ->
                        EvaluationReport(rs.getString("id"),
                                rs.getTimestamp("createdDate").toLocalDateTime(),
                                rs.getString("name"),
                                rs.getString("dimensions").split(","),
                                rs.getString("comment"),
                                rs.getString("improvements").split(","))
                    }.firstOrNull()
        }
    }
}