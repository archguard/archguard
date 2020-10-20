package com.thoughtworks.archgard.scanner2.infrastructure.influx

import com.thoughtworks.archgard.scanner2.domain.model.PackageMetric
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PackageMetricsDtoForWriteInfluxDBTest {

    @Test
    fun should_get_influxdb_request_body() {
        val packageMetric = PackageMetric(23, "moduleName", "packageName", 10, 20)
        val dto = PackageMetricsDtoForWriteInfluxDB(packageMetric)

        val result = dto.toInfluxDBRequestBody()
        assertEquals("package_metric,module_name=moduleName,package_name=packageName,system_id=23 fanIn=10,fanOut=20", result)
    }

    @Test
    fun should_get_influxdb_request_body_when_package_empty() {
        val packageMetric = PackageMetric(23, "moduleName", "", 10, 20)
        val dto = PackageMetricsDtoForWriteInfluxDB(packageMetric)

        val result = dto.toInfluxDBRequestBody()
        assertEquals("package_metric,module_name=moduleName,package_name=.,system_id=23 fanIn=10,fanOut=20", result)
    }
}