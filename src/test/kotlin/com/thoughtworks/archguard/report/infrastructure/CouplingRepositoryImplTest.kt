package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.coupling.ClassCoupling
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.web.WebAppConfiguration

@SpringBootTest
@WebAppConfiguration
internal class CouplingRepositoryImplTest {

    @Autowired
    lateinit var couplingRepositoryImpl: CouplingRepositoryImpl

    @Test
    @Sql("classpath:sqls/insert_jclass_and_class_coupling.sql")
    fun should_get_data_clumps_total_count() {
        val classCouplings: List<ClassCoupling> = couplingRepositoryImpl.getCoupling(6)
        val classCoupling1 = ClassCoupling("ff5b0ec2-23d2-439a-9c7f-5075f84a8861", moduleName = "dubbo-samples-configcenter-xml", packageName = "org.apache.dubbo.samples.configcenter.api", typeName = "DemoService", fanIn = 4, fanOut = 0, coupling = 0.75, instability = 0.0)
        val classCoupling2 = ClassCoupling("ffaa84b1-b849-43f1-ab27-3f70d23057e1", moduleName = "dubbo-samples-consul", packageName = "org.apache.dubbo.samples.consul.api", typeName = "DemoService", fanIn = 1, fanOut = 0, coupling = 0.0, instability = 0.0)
        val classCoupling3 = ClassCoupling("fe8f5fd7-d3d5-4787-9ad1-da227b3c1a7c", moduleName = "dubbo-samples-direct", packageName = "org.apache.dubbo.samples.direct.api", typeName = "DirectService", fanIn = 1, fanOut = 0, coupling = 0.0, instability = 0.0)
        assertThat(classCouplings).containsExactly(classCoupling1, classCoupling2, classCoupling3)
    }

}