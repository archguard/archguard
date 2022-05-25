package com.thoughtworks.archguard.metrics.infrastructure

import com.thoughtworks.archguard.code.module.domain.model.JClassVO
import com.thoughtworks.archguard.metrics.domain.coupling.ClassCoupling
import com.thoughtworks.archguard.metrics.infrastructure.influx.ClassCouplingInfluxDTO
import com.thoughtworks.archguard.metrics.infrastructure.influx.ClassCouplingListInfluxDTO
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ClassCouplingListInfluxDTOTest {
    @Test
    internal fun should_generate_influx_request_string() {
        val classCouplings = listOf(
            ClassCoupling(JClassVO("net.aimeizi.dubbo.service.service.UserService", "dubbo-service"), 0, 0, 0, 4),
            ClassCoupling(JClassVO("net.aimeizi.dubbo.service.service.OtherService", "dubbo-service"), 0, 0, 1, 3)
        )
        val classCouplingDtoListForWriteInfluxDB = ClassCouplingListInfluxDTO(0, classCouplings)

        val influxDBRequestBody = classCouplingDtoListForWriteInfluxDB.toRequestBody()

        assertEquals(
            "metric_class_coupling,class_name=net.aimeizi.dubbo.service.service.UserService,package_name=net.aimeizi.dubbo.service.service,module_name=dubbo-service,system_id=0 inner_fan_in=0,inner_fan_out=0,outer_fan_in=0,outer_fan_out=4,inner_instability=0.0,inner_coupling=0.0,outer_instability=1.0,outer_coupling=0.75\n" +
                "metric_class_coupling,class_name=net.aimeizi.dubbo.service.service.OtherService,package_name=net.aimeizi.dubbo.service.service,module_name=dubbo-service,system_id=0 inner_fan_in=0,inner_fan_out=0,outer_fan_in=1,outer_fan_out=3,inner_instability=0.0,inner_coupling=0.0,outer_instability=0.75,outer_coupling=0.75",
            influxDBRequestBody
        )
    }

    @Test
    internal fun should_convert_object_to_influx_request_body() {
        val classCouplingDtoForWriteInfluxDB = ClassCouplingInfluxDTO(
            0,
            ClassCoupling(JClassVO("net.aimeizi.dubbo.service.service.UserService", "dubbo-service"), 0, 0, 4, 0)
        )

        val toInfluxDBRequestBody = classCouplingDtoForWriteInfluxDB.toInfluxDBRequestBody()

        assertEquals(
            "metric_class_coupling,class_name=net.aimeizi.dubbo.service.service.UserService," +
                "package_name=net.aimeizi.dubbo.service.service,module_name=dubbo-service,system_id=0 " +
                "inner_fan_in=0,inner_fan_out=0,outer_fan_in=4,outer_fan_out=0,inner_instability=0.0," +
                "inner_coupling=0.0,outer_instability=0.0,outer_coupling=0.75",
            toInfluxDBRequestBody
        )
    }
}
