package com.thoughtworks.archgard.scanner2.infrastructure.influx

import com.thoughtworks.archgard.scanner2.domain.model.ModuleMetric

data class ModuleMetricsDtoListForWriteInfluxDB(val moduleMetrics: List<ModuleMetric>) {
    fun toRequestBody() = moduleMetrics.joinToString("\n") { ModuleMetricsDtoForWriteInfluxDB(it).toInfluxDBRequestBody() }
}

data class ModuleMetricsDtoForWriteInfluxDB(val moduleMetric: ModuleMetric) {
    fun toInfluxDBRequestBody(): String {
        return "module_metric,module_name=${moduleMetric.moduleName},system_id=${moduleMetric.systemId} " +
                "fanIn=${moduleMetric.fanIn},fanOut=${moduleMetric.fanOut}"
    }

}
