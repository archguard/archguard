package com.thoughtworks.archguard.scanner2.domain.service

import com.thoughtworks.archguard.scanner2.domain.Toggle
import com.thoughtworks.archguard.scanner2.domain.model.JClassVO
import org.archguard.model.Dependency
import org.archguard.model.code.JClass
import com.thoughtworks.archguard.scanner2.domain.repository.JClassRepository
import com.thoughtworks.archguard.scanner2.domain.repository.JMethodRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ScannerCircularDependencyServiceImplTest {
    private lateinit var scannerCircularDependencyServiceImpl: ScannerCircularDependencyServiceImpl

    @MockK
    private lateinit var jClassRepository: JClassRepository

    @MockK
    private lateinit var jMethodRepository: JMethodRepository

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        scannerCircularDependencyServiceImpl = ScannerCircularDependencyServiceImpl(jClassRepository, jMethodRepository)
    }

    @Test
    fun should_find_cycles_in_graph() {
        val projectId = 1L
        val dependency1 = Dependency("a", "b")
        val dependency2 = Dependency("b", "c")
        val dependency3 = Dependency("c", "d")
        val dependency4 = Dependency("d", "a")

        val dependency5 = Dependency("m", "n")
        val dependency6 = Dependency("n", "p")
        val dependency7 = Dependency("p", "m")
        every { jClassRepository.getDistinctClassDependenciesAndNotThirdParty(projectId) } returns listOf(
            dependency1,
            dependency2,
            dependency3,
            dependency4,
            dependency5,
            dependency6,
            dependency7
        )
        val jClassA = JClass("a", "a", "m1")
        val jClassB = JClass("b", "b", "m1")
        val jClassC = JClass("c", "c", "m1")
        val jClassD = JClass("d", "d", "m1")
        val jClassM = JClass("m", "m", "m1")
        val jClassN = JClass("n", "n", "m1")
        val jClassP = JClass("p", "p", "m1")
        every { jClassRepository.getJClassesNotThirdPartyAndNotTest(projectId) } returns listOf(
            jClassA,
            jClassB,
            jClassC,
            jClassD,
            jClassM,
            jClassN,
            jClassN,
            jClassP
        )
        val classCircularDependency = scannerCircularDependencyServiceImpl.getClassCircularDependency(projectId)
        classCircularDependency.sortedWith(compareBy { it.size })
        assertThat(classCircularDependency.size).isEqualTo(2)
        assertThat(classCircularDependency[0]).containsExactlyInAnyOrderElementsOf(
            listOf(
                JClassVO.fromClass(jClassA),
                JClassVO.fromClass(jClassB),
                JClassVO.fromClass(jClassC),
                JClassVO.fromClass(jClassD)
            )
        )
        assertThat(classCircularDependency[1]).containsExactlyInAnyOrderElementsOf(
            listOf(
                JClassVO.fromClass(jClassM),
                JClassVO.fromClass(jClassN),
                JClassVO.fromClass(jClassP)
            )
        )
    }

    @Test
    fun should_filter_out_internal_class_cycles_in_graph_when_toggle_true() {
        val projectId = 2L
        val dependency1 = Dependency("a", "b")
        val dependency2 = Dependency("b", "c")
        val dependency3 = Dependency("c", "d")
        val dependency4 = Dependency("d", "a")

        val dependency5 = Dependency("m", "m$1")
        val dependency6 = Dependency("m$1", "m")
        every { jClassRepository.getDistinctClassDependenciesAndNotThirdParty(projectId) } returns listOf(
            dependency1,
            dependency2,
            dependency3,
            dependency4,
            dependency5,
            dependency6
        )
        val jClassA = JClass("a", "a", "m1")
        val jClassB = JClass("b", "b", "m1")
        val jClassC = JClass("c", "c", "m1")
        val jClassD = JClass("d", "d", "m1")
        val jClassM = JClass("m", "m", "m1")
        val jClassN = JClass("m$1", "m$1", "m1")
        every { jClassRepository.getJClassesNotThirdPartyAndNotTest(projectId) } returns listOf(
            jClassA,
            jClassB,
            jClassC,
            jClassD,
            jClassM,
            jClassN,
            jClassN
        )
        val classCircularDependency = scannerCircularDependencyServiceImpl.getClassCircularDependency(projectId)
        println(classCircularDependency)

        classCircularDependency.sortedWith(compareBy { it.size })
        assertThat(classCircularDependency.size).isEqualTo(1)
        assertThat(classCircularDependency[0]).containsExactlyInAnyOrderElementsOf(
            listOf(
                JClassVO.fromClass(jClassA),
                JClassVO.fromClass(jClassB),
                JClassVO.fromClass(jClassC),
                JClassVO.fromClass(jClassD)
            )
        )
    }

    @Test
    fun should_include_internal_class_cycles_in_graph_when_toggle_false() {
        Toggle.EXCLUDE_INTERNAL_CLASS_CYCLE_DEPENDENCY.setStatus(false)
        val projectId = 2L
        val dependency1 = Dependency("a", "b")
        val dependency2 = Dependency("b", "c")
        val dependency3 = Dependency("c", "d")
        val dependency4 = Dependency("d", "a")

        val dependency5 = Dependency("m", "m$1")
        val dependency6 = Dependency("m$1", "m")
        every { jClassRepository.getDistinctClassDependenciesAndNotThirdParty(projectId) } returns listOf(
            dependency1,
            dependency2,
            dependency3,
            dependency4,
            dependency5,
            dependency6
        )
        val jClassA = JClass("a", "a", "m1")
        val jClassB = JClass("b", "b", "m1")
        val jClassC = JClass("c", "c", "m1")
        val jClassD = JClass("d", "d", "m1")
        val jClassM = JClass("m", "m", "m1")
        val jClassN = JClass("m$1", "m$1", "m1")
        every { jClassRepository.getJClassesNotThirdPartyAndNotTest(projectId) } returns listOf(
            jClassA,
            jClassB,
            jClassC,
            jClassD,
            jClassM,
            jClassN,
            jClassN
        )
        val classCircularDependency = scannerCircularDependencyServiceImpl.getClassCircularDependency(projectId)
        println(classCircularDependency)

        classCircularDependency.sortedWith(compareBy { it.size })
        assertThat(classCircularDependency.size).isEqualTo(2)
        assertThat(classCircularDependency[0]).containsExactlyInAnyOrderElementsOf(
            listOf(
                JClassVO.fromClass(jClassA),
                JClassVO.fromClass(jClassB),
                JClassVO.fromClass(jClassC),
                JClassVO.fromClass(jClassD)
            )
        )
        assertThat(classCircularDependency[1]).containsExactlyInAnyOrderElementsOf(
            listOf(
                JClassVO.fromClass(jClassM),
                JClassVO.fromClass(jClassN)
            )
        )
    }
}
