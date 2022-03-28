package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.qualitygate.CouplingQualityGate
import com.thoughtworks.archguard.report.domain.qualitygate.QualityGateClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate


@Component
class QualityGateClientImpl(@Value("\${client.host}") val baseUrl: String) : QualityGateClient {
    override fun getQualityGate(qualityGateName: String): CouplingQualityGate {
        val couplingQualityGate = RestTemplate().getForObject("$baseUrl/api/quality-gate-profile/$qualityGateName", CouplingQualityGate::class.java)
        return couplingQualityGate ?: CouplingQualityGate(null, qualityGateName, emptyList(), null, null)
    }
}