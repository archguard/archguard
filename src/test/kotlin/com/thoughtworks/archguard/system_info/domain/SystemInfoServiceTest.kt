package com.thoughtworks.archguard.system_info.domain

import com.github.database.rider.core.api.dataset.DataSet
import com.github.database.rider.spring.api.DBRider
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@DBRider
@SpringBootTest
internal class SystemInfoServiceTest {

    @Autowired
    lateinit var systemInfoService: SystemInfoService

    @AfterEach
    @DataSet("expect/empty_system_info.yml")
    internal fun tearDown() {
    }

    @Test
    fun add_system_info() {
        systemInfoService.analysisClientProxy = DummyAnalysisClientProxy()
        val info = SystemInfo(null, "addSystemInfo", "repo", "sql",
                "username", "password", ScannedType.SCANNED, 1,
                "GIT", null, 1, "master", "jvm")

        val id = systemInfoService.addSystemInfo(info)
        val info1 = systemInfoService.getSystemInfo(id)
        assertEquals("addSystemInfo", info1.systemName)
    }

    @Test
    @DataSet("expect/system_info_api_atest.yml")
    fun update_system_info() {
        systemInfoService.analysisClientProxy = DummyAnalysisClientProxy()

        val info = SystemInfo(1, "systemName1", "repo1", "sql1",
                "username1", "WCA5RH/O9J4yxgU40Z+thg==", ScannedType.NONE, 1,
                "GIT", null, 2, "master", "jvm")
        systemInfoService.updateSystemInfo(info)

        val info1 = systemInfoService.getSystemInfo(1)
        assertEquals("systemName1", info1.systemName)
        assertEquals(2, info1.badSmellThresholdSuiteId)
    }
}