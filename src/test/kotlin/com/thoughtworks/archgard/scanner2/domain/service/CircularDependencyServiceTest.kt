package com.thoughtworks.archgard.scanner2.domain.service

import com.thoughtworks.archgard.scanner2.domain.model.JClass
import com.thoughtworks.archgard.scanner2.domain.repository.JClassRepository
import com.thoughtworks.archgard.scanner2.domain.repository.JMethodRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CircularDependencyServiceTest {
    private lateinit var circularDependencyService: CircularDependencyService

    @MockK
    private lateinit var jClassRepository: JClassRepository

    @MockK
    private lateinit var jMethodRepository: JMethodRepository

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        circularDependencyService = CircularDependencyService(jClassRepository, jMethodRepository)
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
        every { jClassRepository.getAllClassDependencies(projectId) } returns listOf(dependency1, dependency2, dependency3, dependency4, dependency5, dependency6, dependency7)
        val jClassA = JClass("a", "a", "m1")
        val jClassB = JClass("b", "b", "m1")
        val jClassC = JClass("c", "c", "m1")
        val jClassD = JClass("d", "d", "m1")
        val jClassM = JClass("m", "m", "m1")
        val jClassN = JClass("n", "n", "m1")
        val jClassP = JClass("p", "p", "m1")
        every { jClassRepository.getJClassesHasModules(projectId) } returns listOf(jClassA, jClassB, jClassC, jClassD, jClassM, jClassN, jClassN, jClassP)
        val classCircularDependency = circularDependencyService.getClassCircularDependency(projectId)
        classCircularDependency.sortedWith(compareBy { it.size })
        assertThat(classCircularDependency.size).isEqualTo(2)
        assertThat(classCircularDependency[0]).containsExactlyInAnyOrderElementsOf(listOf(jClassA.toVO(), jClassB.toVO(), jClassC.toVO(), jClassD.toVO()))
        assertThat(classCircularDependency[1]).containsExactlyInAnyOrderElementsOf(listOf(jClassM.toVO(), jClassN.toVO(), jClassP.toVO()))
    }
}