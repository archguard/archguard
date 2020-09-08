package com.thoughtworks.archguard.report.domain.coupling


interface CouplingRepository {
    fun getCoupling(systemId: Long, classFanInThreshold: Int, classFanOutThreshold: Int, offset: Long, limit: Long, orderByFanIn: Boolean): List<ClassCoupling>
    fun getCouplingCount(systemId: Long, classFanInThreshold: Int, classFanOutThreshold: Int): Long
    fun getAllCoupling(systemId: Long): List<ClassCoupling>
}