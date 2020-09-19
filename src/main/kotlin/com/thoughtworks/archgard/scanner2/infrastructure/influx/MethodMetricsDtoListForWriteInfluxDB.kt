package com.thoughtworks.archgard.scanner2.infrastructure.influx

import com.thoughtworks.archgard.scanner2.domain.model.MethodMetric

data class MethodMetricsDtoListForWriteInfluxDB(val methodMetrics: List<MethodMetric>) {
    fun toRequestBody() = methodMetrics.joinToString("\n") { MethodMetricsDtoForWriteInfluxDB(it).toInfluxDBRequestBody() }
}

data class MethodMetricsDtoForWriteInfluxDB(val methodMetric: MethodMetric) {
    fun toInfluxDBRequestBody(): String {
        return "method_metric," +
                "method_name=${methodMetric.jMethodVO.name}(${methodMetric.jMethodVO.argumentTypes.joinToString(separator = "\\,")})," +
                "class_name=${methodMetric.jMethodVO.clazz.getTypeName()}," +
                "package_name=${methodMetric.jMethodVO.clazz.getPackageName()}," +
                "module_name=${methodMetric.jMethodVO.clazz.module}," +
                "system_id=${methodMetric.systemId} " +
                "fanIn=${methodMetric.fanIn},fanOut=${methodMetric.fanOut}"
    }
}
