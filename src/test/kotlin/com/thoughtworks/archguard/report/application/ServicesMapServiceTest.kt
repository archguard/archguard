package com.thoughtworks.archguard.report.application

import com.thoughtworks.archguard.report.domain.githotfile.ContainerServiceRepo
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

internal class ServicesMapServiceTest {
    @MockK
    lateinit var repo: ContainerServiceRepo
    private lateinit var service: ServicesMapService

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        service = ServicesMapService(repo)
    }

    @Test
    fun replaceDefaultParameterUrl() {
        val url = service.updateUrl("/api/systems/{systemId}/code-tree/")
        assertEquals("/api/systems/@uri@/code-tree/", url)
    }

    @Test
    fun replaceTwoParametersUrl() {
        val url = service.updateUrl("/api/systems/{systemId}/code-tree/{id}")
        assertEquals("/api/systems/@uri@/code-tree/@uri@", url)
    }
}