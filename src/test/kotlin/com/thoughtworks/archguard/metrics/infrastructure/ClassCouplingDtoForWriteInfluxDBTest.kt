package com.thoughtworks.archguard.metrics.infrastructure

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ClassCouplingDtoForWriteInfluxDBTest {
    @Test
    internal fun shouldConvertObjectToInfluxDBRequestBody() {
        var classCouplingDtoForWriteInfluxDB = ClassCouplingDtoForWriteInfluxDB(
                "net.aimeizi.dubbo.service.service.UserService",
                "net.aimeizi.dubbo.service.service", "dubbo-service", 
                0, 0, 4, 0,
                0.0, 0.0, 0.0, 0.75);

        val toInfluxDBRequestBody = classCouplingDtoForWriteInfluxDB.toInfluxDBRequestBody();
        
        assertEquals("class_coupling,class_name=net.aimeizi.dubbo.service.service.UserService," +
                "package_name=net.aimeizi.dubbo.service.service,module_name=dubbo-service " +
                "inner_fan_in=0,inner_fan_out=0,outer_fan_in=4,outer_fan_out=0,inner_instability=0.0," +
                "inner_coupling=0.0,outer_instability=0.0,outer_coupling=0.75", toInfluxDBRequestBody)
    }
}