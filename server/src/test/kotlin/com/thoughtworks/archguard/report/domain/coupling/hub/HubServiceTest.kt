package com.thoughtworks.archguard.report.domain.coupling.hub;

import com.thoughtworks.archguard.report.domain.badsmell.ThresholdSuiteService
import com.thoughtworks.archguard.report.domain.coupling.hub.ClassCoupling
import com.thoughtworks.archguard.report.domain.coupling.hub.ClassCouplingRepository
import com.thoughtworks.archguard.report.domain.coupling.hub.MethodCoupling
import com.thoughtworks.archguard.report.domain.coupling.hub.MethodCouplingRepository
import com.thoughtworks.archguard.report.domain.coupling.hub.ModuleCoupling
import com.thoughtworks.archguard.report.domain.coupling.hub.ModuleCouplingRepository
import com.thoughtworks.archguard.report.domain.coupling.hub.PackageCoupling
import com.thoughtworks.archguard.report.domain.coupling.hub.PackageCouplingRepository
import org.archguard.smell.BadSmellType
import org.archguard.threshold.ThresholdKey
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class HubServiceTest {

    private val thresholdSuiteService = mock(ThresholdSuiteService::class.java)
    private val classCouplingRepository = mock(ClassCouplingRepository::class.java)
    private val methodCouplingRepository = mock(MethodCouplingRepository::class.java)
    private val packageCouplingRepository = mock(PackageCouplingRepository::class.java)
    private val moduleCouplingRepository = mock(ModuleCouplingRepository::class.java)

    private val hubService = HubService(
        thresholdSuiteService,
        classCouplingRepository,
        methodCouplingRepository,
        packageCouplingRepository,
        moduleCouplingRepository
    )

    @Test
    fun shouldGetClassHubListAboveThreshold() {
        // given
        val systemId = 1L
        val limit = 10L
        val offset = 0L
        val orderByFanIn = true
        val threshold = 5
        val classesCount = 20L
        val classesAboveThreshold = listOf<ClassCoupling>()

        `when`(thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.COUPLING_HUB_CLASS)).thenReturn(threshold)
        `when`(classCouplingRepository.getCouplingAboveThresholdCount(systemId, threshold, threshold)).thenReturn(classesCount)
        `when`(classCouplingRepository.getCouplingAboveThreshold(systemId, threshold, threshold, offset, limit, orderByFanIn)).thenReturn(classesAboveThreshold)

        // when
        val result = hubService.getClassHubListAboveThreshold(systemId, limit, offset, orderByFanIn)

        // then
        assert(result.first == classesAboveThreshold)
        assert(result.second == classesCount)
        assert(result.third == threshold)
    }

    @Test
    fun shouldGetMethodHubListAboveThreshold() {
        // given
        val systemId = 1L
        val limit = 10L
        val offset = 0L
        val orderByFanIn = true
        val threshold = 5
        val count = 20L
        val methodsAboveThreshold = listOf<MethodCoupling>()

        `when`(thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.COUPLING_HUB_METHOD)).thenReturn(threshold)
        `when`(methodCouplingRepository.getCouplingAboveThresholdCount(systemId, threshold, threshold)).thenReturn(count)
        `when`(methodCouplingRepository.getCouplingAboveThreshold(systemId, threshold, threshold, offset, limit, orderByFanIn)).thenReturn(methodsAboveThreshold)

        // when
        val result = hubService.getMethodHubListAboveThreshold(systemId, limit, offset, orderByFanIn)

        // then
        assert(result.first == methodsAboveThreshold)
        assert(result.second == count)
        assert(result.third == threshold)
    }

    @Test
    fun shouldGetPackageHubListAboveThreshold() {
        // given
        val systemId = 1L
        val limit = 10L
        val offset = 0L
        val orderByFanIn = true
        val threshold = 5
        val count = 20L
        val packagesAboveThreshold = listOf<PackageCoupling>()

        `when`(thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.COUPLING_HUB_PACKAGE)).thenReturn(threshold)
        `when`(packageCouplingRepository.getCouplingAboveThresholdCount(systemId, threshold, threshold)).thenReturn(count)
        `when`(packageCouplingRepository.getCouplingAboveThreshold(systemId, threshold, threshold, offset, limit, orderByFanIn)).thenReturn(packagesAboveThreshold)

        // when
        val result = hubService.getPackageHubListAboveThreshold(systemId, limit, offset, orderByFanIn)

        // then
        assert(result.first == packagesAboveThreshold)
        assert(result.second == count)
        assert(result.third == threshold)
    }

    @Test
    fun shouldGetModuleHubListAboveThreshold() {
        // given
        val systemId = 1L
        val limit = 10L
        val offset = 0L
        val orderByFanIn = true
        val threshold = 5
        val count = 20L
        val modulesAboveThreshold = listOf<ModuleCoupling>()

        `when`(thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.COUPLING_HUB_MODULE)).thenReturn(threshold)
        `when`(moduleCouplingRepository.getCouplingAboveThresholdCount(systemId, threshold, threshold)).thenReturn(count)
        `when`(moduleCouplingRepository.getCouplingAboveThreshold(systemId, threshold, threshold, offset, limit, orderByFanIn)).thenReturn(modulesAboveThreshold)

        // when
        val result = hubService.getModuleHubListAboveThreshold(systemId, limit, offset, orderByFanIn)

        // then
        assert(result.first == modulesAboveThreshold)
        assert(result.second == count)
        assert(result.third == threshold)
    }

    @Test
    fun shouldGetHubReport() {
        // given
        val systemId = 1L
        val moduleThreshold = 5
        val packageThreshold = 10
        val classThreshold = 15
        val methodThreshold = 20
        val classCount = 5L
        val packageCount = 10L
        val moduleCount = 15L
        val methodCount = 20L

        `when`(thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.COUPLING_HUB_MODULE)).thenReturn(moduleThreshold)
        `when`(thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.COUPLING_HUB_PACKAGE)).thenReturn(packageThreshold)
        `when`(thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.COUPLING_HUB_CLASS)).thenReturn(classThreshold)
        `when`(thresholdSuiteService.getThresholdValue(systemId, ThresholdKey.COUPLING_HUB_METHOD)).thenReturn(methodThreshold)
        `when`(classCouplingRepository.getCouplingAboveThresholdCount(systemId, classThreshold, classThreshold)).thenReturn(classCount)
        `when`(packageCouplingRepository.getCouplingAboveThresholdCount(systemId, packageThreshold, packageThreshold)).thenReturn(packageCount)
        `when`(moduleCouplingRepository.getCouplingAboveThresholdCount(systemId, moduleThreshold, moduleThreshold)).thenReturn(moduleCount)
        `when`(methodCouplingRepository.getCouplingAboveThresholdCount(systemId, methodThreshold, methodThreshold)).thenReturn(methodCount)

        // when
        val result = hubService.getHubReport(systemId)

        // then
        assert(result[BadSmellType.CLASSHUB] == classCount)
        assert(result[BadSmellType.PACKAGEHUB] == packageCount)
        assert(result[BadSmellType.MODULEHUB] == moduleCount)
        assert(result[BadSmellType.METHODHUB] == methodCount)
    }
}
