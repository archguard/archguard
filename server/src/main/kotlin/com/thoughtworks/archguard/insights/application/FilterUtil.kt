package com.thoughtworks.archguard.insights.application

import org.archguard.domain.insight.CombinatorType
import org.archguard.domain.insight.Query
import org.archguard.domain.insight.RegexQuery

fun <T> postFilter(query: Query, models: List<T>, filter: (data: T, condition: RegexQuery) -> Boolean): List<T> {
    val postqueries = query.postqueries
    if (postqueries.isEmpty()) {
        return models
    } else if (postqueries.size == 1) {
        return models.filter {
            val regexQuery = postqueries[0]
            filter(it, regexQuery)
        }
    } else {
        var finalResult = models
        for (condition in postqueries) {
            finalResult = when (condition.relation) {
                null, CombinatorType.And -> {
                    // first one and all `and`s
                    finalResult.filter {
                        filter(it, condition)
                    }
                }
                CombinatorType.Or -> {
                    val newOnes = models.filter {
                        filter(it, condition)
                    }

                    (finalResult union newOnes).toList()
                }
                else -> {
                    throw java.lang.RuntimeException("Invalid relation in the query")
                }
            }
        }
        return finalResult
    }
}