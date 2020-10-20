package com.thoughtworks.archgard.scanner2.infrastructure.influx

import com.thoughtworks.archgard.scanner2.domain.model.MethodMetric

data class MethodMetricsDtoListForWriteInfluxDB(val methodMetrics: List<MethodMetric>) {
    fun toRequestBody() = methodMetrics.joinToString("\n") { MethodMetricsDtoForWriteInfluxDB(it).toInfluxDBRequestBody() }
}

data class MethodMetricsDtoForWriteInfluxDB(val methodMetric: MethodMetric) {
    fun toInfluxDBRequestBody(): String {
        val methodName = methodMetric.jMethodVO.name.replace(" ", "_")
        val packageName = if (methodMetric.jMethodVO.clazz.getPackageName().isEmpty()) "." else methodMetric.jMethodVO.clazz.getPackageName()

        return "method_metric," +
                "method_name=${methodName}(${methodMetric.jMethodVO.argumentTypes.joinToString(separator = "\\,")})," +
                "class_name=${methodMetric.jMethodVO.clazz.getTypeName()}," +
                "package_name=${packageName}," +
                "module_name=${methodMetric.jMethodVO.clazz.module}," +
                "system_id=${methodMetric.systemId} " +
                "fanIn=${methodMetric.fanIn},fanOut=${methodMetric.fanOut}"
    }
}
