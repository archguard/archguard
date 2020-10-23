package com.thoughtworks.archguard.report.application

import com.thoughtworks.archguard.report.domain.coupling.hub.ClassCoupling
import com.thoughtworks.archguard.report.domain.coupling.hub.ClassCouplingRepository
import com.thoughtworks.archguard.report.domain.qualitygate.ComparationOperator
import com.thoughtworks.archguard.report.domain.qualitygate.CouplingQualityGate
import com.thoughtworks.archguard.report.domain.qualitygate.LayerType
import com.thoughtworks.archguard.report.domain.qualitygate.QualityGateClient
import com.thoughtworks.archguard.report.domain.qualitygate.QualityGateConfig
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ClassCouplingAppServiceTest {
    @MockK
    lateinit var classCouplingRepository: ClassCouplingRepository

    @MockK
    lateinit var qualityGateClient: QualityGateClient

    private lateinit var classCouplingAppService: ClassCouplingAppService

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        classCouplingAppService = ClassCouplingAppService(classCouplingRepository, qualityGateClient)
    }

    @Test
    fun should_filter_not_pass_quality_gateway_coupling_data() {
        val systemId = 1L
        val qualityGateName = "qualityGate1"
        val classCoupling1 = ClassCoupling("id1", "m1", "p1", "c1", 10, 15, 0.8, 0.6)
        val classCoupling2 = ClassCoupling("id2", "m1", "p1", "c2", 5, 5, 0.5, 0.1)
        val classCoupling3 = ClassCoupling("id3", "m1", "p1", "c3", 6, 20, 1.0, 0.7)
        val classCoupling4 = ClassCoupling("id4", "m1", "p1", "c4", 8, 20, 0.1, 1.0)
        every { classCouplingRepository.getAllCoupling(systemId) } returns listOf(classCoupling1, classCoupling2, classCoupling3, classCoupling4)

        val qualityGateConfig1 = QualityGateConfig(LayerType.COUPLINGS, "fanIn", ComparationOperator.LESS, 6)
        val qualityGateConfig2 = QualityGateConfig(LayerType.COUPLINGS, "instability", ComparationOperator.BIGGER, 0.8)
        val qualityGateConfig3 = QualityGateConfig(LayerType.COUPLINGS, "fanOut", ComparationOperator.EQUAL, 20)
        every { qualityGateClient.getQualityGate(qualityGateName) } returns CouplingQualityGate(1L, "qualityGate1", listOf(qualityGateConfig1, qualityGateConfig2, qualityGateConfig3), null, null)

        val couplingFilterByQualityGate = classCouplingAppService.getCouplingFilterByQualityGate(systemId, qualityGateName)
        assertThat(couplingFilterByQualityGate).containsExactlyElementsOf(listOf(classCoupling1))
    }
}