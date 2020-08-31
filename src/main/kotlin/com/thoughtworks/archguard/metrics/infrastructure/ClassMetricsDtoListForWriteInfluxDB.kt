package com.thoughtworks.archguard.metrics.infrastructure

import com.thoughtworks.archguard.metrics.domain.ClassMetric

data class ClassMetricsDtoListForWriteInfluxDB(val classMetrics: List<ClassMetric>) {
    fun toRequestBody() = classMetrics.joinToString("\n") { ClassMetricsDtoForWriteInfluxDB(it).toInfluxDBRequestBody() }
}

data class ClassMetricsDtoForWriteInfluxDB(val classMetric: ClassMetric) {
    fun toInfluxDBRequestBody(): String {
        return "class_metric,class_name=${classMetric.jClassVO.name},package_name=${classMetric.jClassVO.getPackageName()},module_name=${classMetric.jClassVO.module},project_id=${classMetric.systemId} " +
                "abc=${classMetric.abc},noc=${classMetric.noc},dit=${classMetric.dit},lcom4=${classMetric.lcom4}"
    }
}
