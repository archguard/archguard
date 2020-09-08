package com.thoughtworks.archgard.scanner2.infrastructure.influx

import com.thoughtworks.archgard.scanner2.domain.model.ClassCoupling
import com.thoughtworks.archgard.scanner2.domain.model.JClassVO
import com.thoughtworks.archgard.scanner2.infrastructure.influx.ClassCouplingDtoForWriteInfluxDB
import com.thoughtworks.archgard.scanner2.infrastructure.influx.ClassCouplingDtoListForWriteInfluxDB
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ClassMetricsDtoListForWriteInfluxDBTest{
    @Test
    internal fun shouldGenerateInfluxDBRequestString() {
        val classCouplings = listOf(
                ClassCoupling(JClassVO("net.aimeizi.dubbo.service.service.UserService", "dubbo-service"), 0, 0, 0, 4),
                ClassCoupling(JClassVO("net.aimeizi.dubbo.service.service.OtherService", "dubbo-service"), 0, 0, 1, 3)
        )
        val classCouplingDtoListForWriteInfluxDB = ClassCouplingDtoListForWriteInfluxDB(0, classCouplings)

        val influxDBRequestBody = classCouplingDtoListForWriteInfluxDB.toRequestBody()

        assertEquals("class_coupling,class_name=net.aimeizi.dubbo.service.service.UserService,package_name=net.aimeizi.dubbo.service.service,module_name=dubbo-service,system_id=0 inner_fan_in=0,inner_fan_out=0,outer_fan_in=0,outer_fan_out=4,inner_instability=0.0,inner_coupling=0.0,outer_instability=1.0,outer_coupling=0.75\n" +
                "class_coupling,class_name=net.aimeizi.dubbo.service.service.OtherService,package_name=net.aimeizi.dubbo.service.service,module_name=dubbo-service,system_id=0 inner_fan_in=0,inner_fan_out=0,outer_fan_in=1,outer_fan_out=3,inner_instability=0.0,inner_coupling=0.0,outer_instability=0.75,outer_coupling=0.75", influxDBRequestBody)
    }

    @Test
    internal fun shouldConvertObjectToInfluxDBRequestBody() {
        val classCouplingDtoForWriteInfluxDB = ClassCouplingDtoForWriteInfluxDB(0,
                ClassCoupling(JClassVO("net.aimeizi.dubbo.service.service.UserService", "dubbo-service"), 0, 0, 4, 0))

        val toInfluxDBRequestBody = classCouplingDtoForWriteInfluxDB.toInfluxDBRequestBody()

        assertEquals("class_coupling,class_name=net.aimeizi.dubbo.service.service.UserService," +
                "package_name=net.aimeizi.dubbo.service.service,module_name=dubbo-service,system_id=0 " +
                "inner_fan_in=0,inner_fan_out=0,outer_fan_in=4,outer_fan_out=0,inner_instability=0.0," +
                "inner_coupling=0.0,outer_instability=0.0,outer_coupling=0.75", toInfluxDBRequestBody)
    }

}