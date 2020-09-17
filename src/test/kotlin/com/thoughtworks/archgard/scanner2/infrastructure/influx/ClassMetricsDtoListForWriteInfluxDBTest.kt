package com.thoughtworks.archgard.scanner2.infrastructure.influx

import com.thoughtworks.archgard.scanner2.domain.model.ClassMetric
import com.thoughtworks.archgard.scanner2.domain.model.JClassVO
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ClassMetricsDtoListForWriteInfluxDBTest {
    @Test
    internal fun shouldGenerateInfluxDBRequestString() {
        val listOfMetrics = listOf(
                ClassMetric(1,
                        JClassVO("com.service.UserService", "dubbo-service"),
                        1, 1, 1, 1, 5, 10),
                ClassMetric(1,
                        JClassVO("com.service.DemoService", "dubbo-service"),
                        2, 2, 2, 2, 10, 5)
        )
        val dtoListForWriteInfluxDB = ClassMetricsDtoListForWriteInfluxDB(listOfMetrics)

        val influxDBRequestBody = dtoListForWriteInfluxDB.toRequestBody()

        val expect = "class_metric,class_name=UserService,package_name=com.service," +
                "module_name=dubbo-service,system_id=1 abc=1,noc=1,dit=1,lcom4=1,fanIn=5,fanOut=10\n" +
                "class_metric,class_name=DemoService,package_name=com.service," +
                "module_name=dubbo-service,system_id=1 abc=2,noc=2,dit=2,lcom4=2,fanIn=10,fanOut=5"
        assertEquals(expect, influxDBRequestBody)
    }

}