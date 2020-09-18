package com.thoughtworks.archgard.scanner2.infrastructure.influx

import com.thoughtworks.archgard.scanner2.domain.model.PackageMetric

data class PackageMetricsDtoListForWriteInfluxDB(val packageMetrics: List<PackageMetric>) {
    fun toRequestBody() = packageMetrics.joinToString("\n") { PackageMetricsDtoForWriteInfluxDB(it).toInfluxDBRequestBody() }
}

data class PackageMetricsDtoForWriteInfluxDB(private val packageMetric: PackageMetric) {
    fun toInfluxDBRequestBody(): String {
        return "package_metric,module_name=${packageMetric.moduleName},package_name=${packageMetric.packageName},system_id=${packageMetric.systemId} " +
                "fanIn=${packageMetric.fanIn},fanOut=${packageMetric.fanOut}"
    }
}
