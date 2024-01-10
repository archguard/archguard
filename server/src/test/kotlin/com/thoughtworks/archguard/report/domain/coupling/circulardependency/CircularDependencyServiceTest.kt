package com.thoughtworks.archguard.report.domain.coupling.circulardependency;

import com.thoughtworks.archguard.report.domain.models.ClassVO
import com.thoughtworks.archguard.report.domain.models.MethodVO
import com.thoughtworks.archguard.report.domain.models.ModuleVO
import com.thoughtworks.archguard.report.domain.models.PackageVO
import org.archguard.smell.BadSmellType
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class CircularDependencyServiceTest {

    private val circularDependencyRepository = Mockito.mock(CircularDependencyRepository::class.java)
    private val circularDependencyService = CircularDependencyService(circularDependencyRepository)

    @Test
    fun `should get module circular dependency with total count`() {
        // given
        val systemId = 1L
        val limit = 10L
        val offset = 0L
        val circularDependencyList = listOf("module1;module2", "module3;module4")
        val circularDependencyCount = 2L
        val expectedData = listOf(
            CircularDependency(listOf(ModuleVO("module1"), ModuleVO("module2"))),
            CircularDependency(listOf(ModuleVO("module3"), ModuleVO("module4")))
        )
        `when`(circularDependencyRepository.getCircularDependencyCount(systemId, CircularDependencyType.MODULE))
            .thenReturn(circularDependencyCount)
        `when`(circularDependencyRepository.getCircularDependency(systemId, CircularDependencyType.MODULE, limit, offset))
            .thenReturn(circularDependencyList)

        // when
        val result = circularDependencyService.getModuleCircularDependencyWithTotalCount(systemId, limit, offset)

        // then
        verify(circularDependencyRepository).getCircularDependencyCount(systemId, CircularDependencyType.MODULE)
        verify(circularDependencyRepository).getCircularDependency(systemId, CircularDependencyType.MODULE, limit, offset)
        assert(result.first == expectedData)
        assert(result.second == circularDependencyCount)
        assert(result.third == 0)
    }

    @Test
    fun `should get package circular dependency with total count`() {
        // given
        val systemId = 1L
        val limit = 10L
        val offset = 0L
        val circularDependencyList = listOf("org.package1;org.package2", "org.package3;org.package4")
        val circularDependencyCount = 2L
        val expectedData = listOf(
            CircularDependency(listOf(PackageVO.create("org.package1"), PackageVO.create("org.package2"))),
            CircularDependency(listOf(PackageVO.create("org.package3"), PackageVO.create("org.package4")))
        )
        `when`(circularDependencyRepository.getCircularDependencyCount(systemId, CircularDependencyType.PACKAGE))
            .thenReturn(circularDependencyCount)
        `when`(circularDependencyRepository.getCircularDependency(systemId, CircularDependencyType.PACKAGE, limit, offset))
            .thenReturn(circularDependencyList)

        // when
        val result = circularDependencyService.getPackageCircularDependencyWithTotalCount(systemId, limit, offset)

        // then
        verify(circularDependencyRepository).getCircularDependencyCount(systemId, CircularDependencyType.PACKAGE)
        verify(circularDependencyRepository).getCircularDependency(systemId, CircularDependencyType.PACKAGE, limit, offset)
        assert(result.first == expectedData)
        assert(result.second == circularDependencyCount)
        assert(result.third == 0)
    }

    @Test
    fun `should get class circular dependency with total count`() {
        // given
        val systemId = 1L
        val limit = 10L
        val offset = 0L
        val circularDependencyList = listOf("class1;class2", "class3;class4")
        val circularDependencyCount = 2L
        val expectedData = listOf(
            CircularDependency(listOf(ClassVO.create("class1"), ClassVO.create("class2"))),
            CircularDependency(listOf(ClassVO.create("class3"), ClassVO.create("class4")))
        )
        `when`(circularDependencyRepository.getCircularDependencyCount(systemId, CircularDependencyType.CLASS))
            .thenReturn(circularDependencyCount)
        `when`(circularDependencyRepository.getCircularDependency(systemId, CircularDependencyType.CLASS, limit, offset))
            .thenReturn(circularDependencyList)

        // when
        val result = circularDependencyService.getClassCircularDependencyWithTotalCount(systemId, limit, offset)

        // then
        verify(circularDependencyRepository).getCircularDependencyCount(systemId, CircularDependencyType.CLASS)
        verify(circularDependencyRepository).getCircularDependency(systemId, CircularDependencyType.CLASS, limit, offset)
        assert(result.first == expectedData)
        assert(result.second == circularDependencyCount)
        assert(result.third == 0)
    }

    @Test
    fun `should get method circular dependency with total count`() {
        // given
        val systemId = 1L
        val limit = 10L
        val offset = 0L
        val circularDependencyList = listOf("org.pkg.method1();org.pkg.method2()", "org.pkg.method3();org.pkg.method4()")
        val circularDependencyCount = 2L
        val expectedData = listOf(
            CircularDependency(listOf(MethodVO.create("org.pkg.method1()"), MethodVO.create("org.pkg.method2()"))),
            CircularDependency(listOf(MethodVO.create("org.pkg.method3()"), MethodVO.create("org.pkg.method4()")))
        )
        `when`(circularDependencyRepository.getCircularDependencyCount(systemId, CircularDependencyType.METHOD))
            .thenReturn(circularDependencyCount)
        `when`(circularDependencyRepository.getCircularDependency(systemId, CircularDependencyType.METHOD, limit, offset))
            .thenReturn(circularDependencyList)

        // when
        val result = circularDependencyService.getMethodCircularDependencyWithTotalCount(systemId, limit, offset)

        // then
        verify(circularDependencyRepository).getCircularDependencyCount(systemId, CircularDependencyType.METHOD)
        verify(circularDependencyRepository).getCircularDependency(systemId, CircularDependencyType.METHOD, limit, offset)
        assert(result.first == expectedData)
        assert(result.second == circularDependencyCount)
        assert(result.third == 0)
    }

    @Test
    fun `should get circular dependency report`() {
        // given
        val systemId = 1L
        val moduleCount = 2L
        val packageCount = 3L
        val classCount = 4L
        val methodCount = 5L
        val expectedReport = mapOf(BadSmellType.CYCLEDEPENDENCY to (moduleCount + packageCount + classCount + methodCount))
        `when`(circularDependencyRepository.getCircularDependencyCount(systemId, CircularDependencyType.MODULE))
            .thenReturn(moduleCount)
        `when`(circularDependencyRepository.getCircularDependencyCount(systemId, CircularDependencyType.PACKAGE))
            .thenReturn(packageCount)
        `when`(circularDependencyRepository.getCircularDependencyCount(systemId, CircularDependencyType.CLASS))
            .thenReturn(classCount)
        `when`(circularDependencyRepository.getCircularDependencyCount(systemId, CircularDependencyType.METHOD))
            .thenReturn(methodCount)

        // when
        val result = circularDependencyService.getCircularDependencyReport(systemId)

        // then
        verify(circularDependencyRepository).getCircularDependencyCount(systemId, CircularDependencyType.MODULE)
        verify(circularDependencyRepository).getCircularDependencyCount(systemId, CircularDependencyType.PACKAGE)
        verify(circularDependencyRepository).getCircularDependencyCount(systemId, CircularDependencyType.CLASS)
        verify(circularDependencyRepository).getCircularDependencyCount(systemId, CircularDependencyType.METHOD)
        assert(result == expectedReport)
    }
}
