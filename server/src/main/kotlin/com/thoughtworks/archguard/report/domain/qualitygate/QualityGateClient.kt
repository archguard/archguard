package com.thoughtworks.archguard.report.domain.qualitygate

interface QualityGateClient {
    fun getQualityGate(qualityGateName: String): CouplingQualityGate
}
