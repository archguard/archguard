package com.thoughtworks.archguard.module.infrastructure

import com.thoughtworks.archguard.module.domain.DubboConfigRepository
import com.thoughtworks.archguard.module.domain.ReferenceConfig
import com.thoughtworks.archguard.module.domain.SubModule
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.web.WebAppConfiguration

@SpringBootTest
@WebAppConfiguration
internal class DubboConfigRepositoryImplTest {
    @Autowired
    lateinit var dubboConfigRepository: DubboConfigRepository

    @Test
    @Sql("classpath:sqls/dubbo_reference_config.sql")
    internal fun should_get_reference_config_by_interface_name_and_submodule() {
        val referenceConfig = dubboConfigRepository.getReferenceConfigBy("org.apache.dubbo.samples.group.api.GroupService",
                SubModule("ce818d60-f54e-41b7-9851-9b0eb212548d", "group-example", "/path/group-example"))
        assertThat(referenceConfig.size).isEqualTo(2)
        assertThat(referenceConfig[0]).isEqualToComparingFieldByField(ReferenceConfig(id="7a22b4f9-292e-4144-83e5-a140f1ce24fc",
                beanId="groupAService", interfaceName="org.apache.dubbo.samples.group.api.GroupService", version=null,
                group="groupA", subModule=SubModule(id="ce818d60-f54e-41b7-9851-9b0eb212548d", name="group-example", path="/path/group-example")))

        assertThat(referenceConfig[1]).isEqualToComparingFieldByField(ReferenceConfig(id="51423197-4abe-460d-a1d7-8ca7fc85c303",
                beanId="groupBService", interfaceName="org.apache.dubbo.samples.group.api.GroupService", version=null,
                group="groupB", subModule=SubModule(id="ce818d60-f54e-41b7-9851-9b0eb212548d", name="group-example", path="/path/group-example")))



    }
}