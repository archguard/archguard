package com.thoughtworks.archgard.scanner2.infrastructure.influx

import com.thoughtworks.archgard.scanner2.domain.model.JClassVO
import com.thoughtworks.archgard.scanner2.domain.model.JMethodVO
import com.thoughtworks.archgard.scanner2.domain.model.MethodMetric
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class MethodMetricsDtoListForWriteInfluxDBTest {
    @Test
    internal fun shouldGenerateInfluxDBRequestString() {
        val listOfMetrics = listOf(
                MethodMetric(1,
                        JMethodVO("hello", JClassVO("com.service.UserService", "dubbo-service"), "String", listOf("String", "Int")),
                        5, 10),
                MethodMetric(1,
                        JMethodVO("hello", JClassVO("com.service.UserService", "dubbo-service"), "String", listOf("String")),
                        10, 5)
        )
        val dtoListForWriteInfluxDB = MethodMetricsDtoListForWriteInfluxDB(listOfMetrics)

        val influxDBRequestBody = dtoListForWriteInfluxDB.toRequestBody()

        val expect = "method_metric,method_name=hello(String,Int),class_name=UserService,package_name=com.service," +
                "module_name=dubbo-service,system_id=1 fanIn=5,fanOut=10\n" +
                "method_metric,method_name=hello(String),class_name=UserService,package_name=com.service," +
                "module_name=dubbo-service,system_id=1 fanIn=10,fanOut=5"
        Assertions.assertEquals(expect, influxDBRequestBody)
    }
}