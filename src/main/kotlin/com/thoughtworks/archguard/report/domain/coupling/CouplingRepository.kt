package com.thoughtworks.archguard.report.domain.coupling


interface CouplingRepository {
    fun getCoupling(systemId: Long): List<ClassCoupling>
}