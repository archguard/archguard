package com.thoughtworks.archguard.packages.domain

import com.thoughtworks.archguard.module.domain.LogicModuleRepository
import com.thoughtworks.archguard.module.domain.model.SubModule
import com.thoughtworks.archguard.packages.infrastructure.PackageDependenceDTO
import io.mockk.MockKAnnotations.init
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PackageServiceTest {
    @MockK
    lateinit var packageRepository: PackageRepository

    @MockK
    lateinit var moduleRepository: LogicModuleRepository

    @InjectMockKs
    var packageService = PackageService()

    @BeforeEach
    fun setUp() {
        init(this)
    }

    @Test
    internal fun `should get package dependencies by modules`() {
        //given
        val projectId: Long = 1;
        every { moduleRepository.getAllSubModule(projectId) } returns listOf(SubModule("com.module1"), SubModule("com.module2"))
        val dependencies1 = listOf(PackageDependenceDTO("org.wrapper.ThrowablePB\$ThrowableProto",
                "org.test.ThrowablePB\$ThrowableProto"))
        val dependencies2 = listOf<PackageDependenceDTO>()
        every { packageRepository.getPackageDependenceByModule("com.module1") } returns dependencies1
        every { packageRepository.getPackageDependenceByModule("com.module2") } returns dependencies2
        //when
        val packageDependencies = packageService.getPackageDependencies(projectId)
        //then
        assertThat(packageDependencies.size).isEqualTo(2)
        assertThat(packageDependencies[0].packageGraph.nodes.size).isEqualTo(3)
        assertThat(packageDependencies[0].packageGraph.edges.size).isEqualTo(1)
        assertThat(packageDependencies[1].packageGraph.nodes.size).isEqualTo(0)
        assertThat(packageDependencies[1].packageGraph.edges.size).isEqualTo(0)
    }
}
