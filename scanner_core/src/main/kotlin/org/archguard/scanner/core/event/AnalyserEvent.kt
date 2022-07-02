package org.archguard.scanner.core.event

import java.time.Instant

data class AnalyserEvent(
    val type: AnalyserEventType,
    val topic: AnalyserTopic,
    val timestamp: Instant,
    val data: Any,
)

data class AnalyserTopic(
    val taskId: String,
    val systemId: String,
)

enum class AnalyserEventType {
    CODE_DATA_STRUCT,
    API_CONTAINERS,
    ;
}