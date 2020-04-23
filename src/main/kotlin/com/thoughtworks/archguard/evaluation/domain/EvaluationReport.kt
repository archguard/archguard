package com.thoughtworks.archguard.evaluation.domain

import java.time.LocalDateTime

data class EvaluationReport(val id: String?,
                            val createdDate: LocalDateTime,
                            val name: String,
                            val dimensions: List<String>,
                            val comment: String,
                            val improvements: List<String>){
}