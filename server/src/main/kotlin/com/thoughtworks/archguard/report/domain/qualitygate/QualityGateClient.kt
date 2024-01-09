package com.thoughtworks.archguard.report.domain.qualitygate

import org.archguard.gate.CouplingQualityGate

interface QualityGateClient {
    fun getQualityGate(qualityGateName: String): CouplingQualityGate
}
