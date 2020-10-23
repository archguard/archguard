package com.thoughtworks.archguard.api

import com.github.database.rider.core.api.dataset.DataSet
import com.github.database.rider.spring.api.DBRider
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import javax.annotation.Resource

@SpringBootTest
@WebAppConfiguration
@DBRider
class SystemtInfoApiATest {

    @Autowired
    lateinit var jdbi: Jdbi

    @Resource
    private lateinit var wac: WebApplicationContext

    @Test
    @DataSet("expect/system_info_api_atest.yml")
    fun should_get_system_info_when_sent_get_system_info_api_given_there_is_already_system_info_in_database() {
        val request = MockMvcRequestBuilders.request(HttpMethod.GET, "/system-info")
        val result = MockMvcBuilders.webAppContextSetup(wac).build().perform(request)
                .andExpect(status().isOk)
                .andReturn()

        val content = result.response.contentAsString
        val status = result.response.status

        val except = "[{\"id\":1,\"systemName\":\"systemName1\",\"repo\":[\"repo1\"],\"sql\":\"sql1\"" +
                ",\"username\":\"username1\",\"password\":\"WCA5RH/O9J4yxgU40Z+thg==\",\"scanned\":\"NONE\"," +
                "\"qualityGateProfileId\":1,\"repoType\":\"GIT\",\"updatedTime\":1603250641000,\"badSmellThresholdSuiteId\":1,\"branch\":\"master\"}]"

        assertEquals(200, status)
        assertEquals(except, content)
    }

    @Test
    @DataSet("expect/system_info_api_atest.yml")
    fun should_get_system_info_success_when_get_by_id() {
        val request = MockMvcRequestBuilders.request(HttpMethod.GET, "/system-info/1")
        val result = MockMvcBuilders.webAppContextSetup(wac).build().perform(request)
                .andExpect(status().isOk)
                .andReturn()

        val content = result.response.contentAsString
        val status = result.response.status

        val except = "{\"id\":1,\"systemName\":\"systemName1\",\"repo\":[\"repo1\"],\"sql\":\"sql1\"" +
                ",\"username\":\"username1\",\"password\":\"WCA5RH/O9J4yxgU40Z+thg==\",\"scanned\":\"NONE\"," +
                "\"qualityGateProfileId\":1,\"repoType\":\"GIT\",\"updatedTime\":1603250641000,\"badSmellThresholdSuiteId\":1,\"branch\":\"master\"}"
        assertEquals(200, status)
        assertEquals(except, content)


    }

    @Test
    @DataSet("expect/system_info_api_atest.yml")
    fun should_get_success_message_when_sent_update_system_info_api_given_there_is_already_system_info_in_database() {
        val updateDTO = "{\"id\":1,\"systemName\":\"systemName2\",\"repo\":[\"repo2\"],\"sql\":\"sql2\"" +
                ",\"username\":\"username2\",\"password\":\"WCA5RH/O9J4yxgU40Z+thg==\",\"scanned\":\"NONE\"," +
                "\"qualityGateProfileId\":1,\"repoType\":\"GIT\",\"badSmellThresholdSuiteId\":2,\"branch\":\"dev\"}"

        val request = MockMvcRequestBuilders.request(HttpMethod.PUT, "/system-info")
                .contentType("application/json")
                .content(updateDTO)
        val result = MockMvcBuilders.webAppContextSetup(wac).build().perform(request)
                .andExpect(status().isOk)
                .andReturn()

        val status = result.response.status

        assertEquals(200, status)

        val re = jdbi.withHandle<String, Nothing> {
            it.createQuery("select `system_name` from system_info where id = 1")
                    .mapTo(String::class.java)
                    .one()
        }

        assertEquals("systemName2", re)
    }

    @Test
    fun should_get_success_massage_when_sent_add_system_info_api_given_there_is_no_system_info_in_database() {
        jdbi.withHandle<Int, Nothing> {
            it.createUpdate("delete from system_info")
                    .execute()
        }

        val insertDTO = "{\"systemName\":\"systemName3\",\"repo\":[\"repo3\"],\"sql\":\"sql3\"" +
                ",\"username\":\"username3\",\"password\":\"admin123456\",\"repoType\":\"GIT\"}"

        val request = MockMvcRequestBuilders.request(HttpMethod.POST, "/system-info")
                .contentType("application/json")
                .content(insertDTO)
        val result = MockMvcBuilders.webAppContextSetup(wac).build().perform(request)
                .andExpect(status().isOk)
                .andReturn()

        val status = result.response.status

        assertEquals(200, status)

        val re = jdbi.withHandle<List<Long>, Nothing> {
            it.createQuery("select id from system_info where `system_name` = 'systemName3'")
                    .mapTo(Long::class.java)
                    .list()
        }
        assertEquals(1, re.size)

    }

    @Test
    @DataSet("expect/system_info_api_atest.yml")
    fun should_delete_system_info_success_when_get_by_id() {
        val request = MockMvcRequestBuilders.request(HttpMethod.DELETE, "/system-info/1")
        val result = MockMvcBuilders.webAppContextSetup(wac).build().perform(request)
                .andExpect(status().isOk)
                .andReturn()

        val status = result.response.status

        val re = jdbi.withHandle<List<Long>, Nothing> {
            it.createQuery("select id from system_info where `system_name` = 'systemName1'")
                    .mapTo(Long::class.java)
                    .list()
        }

        assertEquals(200, status)
        assertEquals(0, re.size)
    }
}
