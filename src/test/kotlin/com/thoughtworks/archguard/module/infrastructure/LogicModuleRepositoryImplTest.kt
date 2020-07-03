package com.thoughtworks.archguard.module.infrastructure

import com.thoughtworks.archguard.module.domain.LogicModule
import com.thoughtworks.archguard.module.domain.LogicModuleRepository
import com.thoughtworks.archguard.module.domain.LogicModuleStatus
import com.thoughtworks.archguard.module.domain.ModuleMember
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.web.WebAppConfiguration

@SpringBootTest
@WebAppConfiguration
internal class LogicModuleRepositoryImplTest {

    @Autowired
    lateinit var logicModuleRepository: LogicModuleRepository

    @Test
    @Sql("classpath:sqls/insert_logic_module.sql")
    internal fun `should only select normal data from logic module`() {
        val normalLogicModules = logicModuleRepository.getAllByShowStatus(true)
        assertThat(normalLogicModules.size).isEqualTo(1)
        assertThat(normalLogicModules[0]).isEqualTo(
                LogicModule("id1", "dubbo-provider", listOf(ModuleMember.createModuleMember("dubbo-provider")), LogicModuleStatus.NORMAL))
    }
}