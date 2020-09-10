package com.thoughtworks.archguard.report.application

import com.thoughtworks.archguard.report.domain.coupling.ClassCoupling
import com.thoughtworks.archguard.report.domain.coupling.CouplingRepository
import com.thoughtworks.archguard.report.domain.qualitygate.ComparationOperator
import com.thoughtworks.archguard.report.domain.qualitygate.QualityGateClient
import com.thoughtworks.archguard.report.domain.qualitygate.QualityGateConfig
import org.springframework.stereotype.Service

@Service
class ClassCouplingAppService(val classCouplingRepository: CouplingRepository, val qualityGateClient: QualityGateClient) {
    fun getCouplingFilterByQualityGate(systemId: Long, qualityGateName: String): List<ClassCoupling> {
        val allCoupling = classCouplingRepository.getAllCoupling(systemId)
        val couplingQualityGate = qualityGateClient.getQualityGate(qualityGateName)
        val config = couplingQualityGate.config ?: return allCoupling

        return filterByQualityGateConfig(config, allCoupling)
    }

    private fun filterByQualityGateConfig(qualityGateConfigs: List<QualityGateConfig>, couplings: List<ClassCoupling>): List<ClassCoupling> {
        var duplicatedCouplings = couplings
        val fanIn = qualityGateConfigs.firstOrNull { it.quota == "fanIn" }
        if (fanIn != null) {
            duplicatedCouplings = when (fanIn.operator) {
                ComparationOperator.BIGGER -> duplicatedCouplings.filter { it.fanIn > fanIn.value.toInt() }
                ComparationOperator.EQUAL -> duplicatedCouplings.filter { it.fanIn == fanIn.value.toInt() }
                ComparationOperator.LESS -> duplicatedCouplings.filter { it.fanIn < fanIn.value.toInt() }
            }
        }
        val fanOut = qualityGateConfigs.firstOrNull { it.quota == "fanOut" }
        if (fanOut != null) {
            duplicatedCouplings = when (fanOut.operator) {
                ComparationOperator.BIGGER -> duplicatedCouplings.filter { it.fanOut > fanOut.value.toInt() }
                ComparationOperator.EQUAL -> duplicatedCouplings.filter { it.fanOut == fanOut.value.toInt() }
                ComparationOperator.LESS -> duplicatedCouplings.filter { it.fanOut < fanOut.value.toInt() }
            }
        }
        val coupling = qualityGateConfigs.firstOrNull { it.quota == "coupling" }
        if (coupling != null) {
            duplicatedCouplings = when (coupling.operator) {
                ComparationOperator.BIGGER -> duplicatedCouplings.filter { it.coupling > coupling.value.toDouble() }
                ComparationOperator.EQUAL -> duplicatedCouplings.filter { it.coupling == coupling.value.toDouble() }
                ComparationOperator.LESS -> duplicatedCouplings.filter { it.coupling < coupling.value.toDouble() }
            }
        }
        val instability = qualityGateConfigs.firstOrNull { it.quota == "instability" }
        if (instability != null) {
            duplicatedCouplings = when (instability.operator) {
                ComparationOperator.BIGGER -> duplicatedCouplings.filter { it.instability > instability.value.toDouble() }
                ComparationOperator.EQUAL -> duplicatedCouplings.filter { it.instability == instability.value.toDouble() }
                ComparationOperator.LESS -> duplicatedCouplings.filter { it.instability < instability.value.toDouble() }
            }
        }
        return duplicatedCouplings
    }

}