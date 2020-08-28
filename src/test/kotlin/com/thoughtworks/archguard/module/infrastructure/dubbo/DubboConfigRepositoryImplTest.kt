package com.thoughtworks.archguard.module.infrastructure.dubbo

import com.thoughtworks.archguard.module.domain.dubbo.DubboConfigRepository
import com.thoughtworks.archguard.module.domain.dubbo.ReferenceConfig
import com.thoughtworks.archguard.module.domain.dubbo.ServiceConfig
import com.thoughtworks.archguard.module.domain.dubbo.SubModuleDubbo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@WebAppConfiguration
@Transactional
internal class DubboConfigRepositoryImplTest {
    @Autowired
    lateinit var dubboConfigRepository: DubboConfigRepository

    @Test
    @Sql("classpath:sqls/dubbo_reference_config.sql")
    internal fun should_get_reference_config_by_interface_name_and_submodule() {
        val projectId: Long = 1
        val referenceConfig = dubboConfigRepository.getReferenceConfigBy(projectId,
                "org.apache.dubbo.samples.group.api.GroupService",
                SubModuleDubbo("ce818d60-f54e-41b7-9851-9b0eb212548d", "group-example", "/path/group-example"))
        assertThat(referenceConfig.size).isEqualTo(2)
        assertThat(referenceConfig[0]).isEqualToComparingFieldByField(ReferenceConfig(id = "7a22b4f9-292e-4144-83e5-a140f1ce24fc",
                beanId = "groupAService", interfaceName = "org.apache.dubbo.samples.group.api.GroupService", version = null,
                group = "groupA", subModule = SubModuleDubbo(id = "ce818d60-f54e-41b7-9851-9b0eb212548d", name = "group-example", path = "/path/group-example")))

        assertThat(referenceConfig[1]).isEqualToComparingFieldByField(ReferenceConfig(id = "51423197-4abe-460d-a1d7-8ca7fc85c303",
                beanId = "groupBService", interfaceName = "org.apache.dubbo.samples.group.api.GroupService", version = null,
                group = "groupB", subModule = SubModuleDubbo(id = "ce818d60-f54e-41b7-9851-9b0eb212548d", name = "group-example", path = "/path/group-example")))
    }

    @Test
    @Sql("classpath:sqls/dubbo_service_config.sql")
    internal fun should_get_service_config_by_reference_config_when_single_group() {
        val projectId: Long = 1
        val serviceConfigs = dubboConfigRepository.getServiceConfigBy(projectId, ReferenceConfig("id", "id1", "org.apache.dubbo.samples.group.api.GroupService",
                null, "groupA", SubModuleDubbo("ce818d60-f54e-41b7-9851-9b0eb212548d", "dubbo-samples-groupA", "../dubbo-samples/java/dubbo-samples-groupA")))
        assertThat(serviceConfigs.size).isEqualTo(1)
        assertThat(serviceConfigs[0]).isEqualToComparingFieldByField(ServiceConfig(id = "45aca590-994c-4c96-aa0a-5f3df76b8a1e",
                interfaceName = "org.apache.dubbo.samples.group.api.GroupService", ref = "groupAService", version = null,
                group = "groupA", subModule = SubModuleDubbo(id = "ce818d60-f54e-41b7-9851-9b0eb212548d", name = "dubbo-samples-groupA", path = "../dubbo-samples/java/dubbo-samples-groupA")))
    }

    @Test
    @Sql("classpath:sqls/dubbo_service_config.sql")
    internal fun should_get_service_config_by_reference_config_when_all_group() {
        val projectId: Long = 1
        val serviceConfigs = dubboConfigRepository.getServiceConfigBy(projectId, ReferenceConfig("id", "id1", "org.apache.dubbo.samples.group.api.GroupService",
                null, "*", SubModuleDubbo("ce818d60-f54e-41b7-9851-9b0eb212548d", "dubbo-samples-group", "../dubbo-samples/java/dubbo-samples-group")))
        assertThat(serviceConfigs.size).isEqualTo(3)
        assertThat(serviceConfigs).containsAll(listOf(
                ServiceConfig(id = "45aca590-994c-4c96-aa0a-5f3df76b8a1e",
                        interfaceName = "org.apache.dubbo.samples.group.api.GroupService", ref = "groupAService",
                        version = null, group = "groupA",
                        subModule = SubModuleDubbo(id = "ce818d60-f54e-41b7-9851-9b0eb212548d", name = "dubbo-samples-groupA", path = "../dubbo-samples/java/dubbo-samples-groupA")),
                ServiceConfig(id = "dba87323-718c-4046-9184-ae9f250691eb",
                        interfaceName = "org.apache.dubbo.samples.group.api.GroupService", ref = "groupBService",
                        version = null, group = "groupB",
                        subModule = SubModuleDubbo(id = "1debc4fc-5fa9-40ad-9939-b69effd54c0f", name = "dubbo-samples-groupB", path = "../dubbo-samples/java/dubbo-samples-groupB")),
                ServiceConfig(id = "eb650760-389e-4aba-abf7-ac92a377b79b",
                        interfaceName = "org.apache.dubbo.samples.group.api.GroupService", ref = "groupCService",
                        version = null, group = "groupC",
                        subModule = SubModuleDubbo(id = "a4a8b3c5-a17c-42f7-8733-d9823a6c790a", name = "dubbo-samples-groupC", path = "../dubbo-samples/java/dubbo-samples-groupC"))))
    }

    @Test
    @Sql("classpath:sqls/dubbo_service_config.sql")
    internal fun should_get_service_config_by_reference_config_when_multi_group() {
        val projectId: Long = 1
        val serviceConfigs = dubboConfigRepository.getServiceConfigBy(projectId, ReferenceConfig("id", "id1", "org.apache.dubbo.samples.group.api.GroupService",
                null, "groupA, groupB", SubModuleDubbo("ce818d60-f54e-41b7-9851-9b0eb212548d", "dubbo-samples-group", "../dubbo-samples/java/dubbo-samples-group")))
        assertThat(serviceConfigs.size).isEqualTo(2)
        assertThat(serviceConfigs).containsAll(listOf(
                ServiceConfig(id = "45aca590-994c-4c96-aa0a-5f3df76b8a1e",
                        interfaceName = "org.apache.dubbo.samples.group.api.GroupService", ref = "groupAService",
                        version = null, group = "groupA",
                        subModule = SubModuleDubbo(id = "ce818d60-f54e-41b7-9851-9b0eb212548d", name = "dubbo-samples-groupA", path = "../dubbo-samples/java/dubbo-samples-groupA")),
                ServiceConfig(id = "dba87323-718c-4046-9184-ae9f250691eb",
                        interfaceName = "org.apache.dubbo.samples.group.api.GroupService", ref = "groupBService",
                        version = null, group = "groupB",
                        subModule = SubModuleDubbo(id = "1debc4fc-5fa9-40ad-9939-b69effd54c0f", name = "dubbo-samples-groupB", path = "../dubbo-samples/java/dubbo-samples-groupB"))))
    }
}
