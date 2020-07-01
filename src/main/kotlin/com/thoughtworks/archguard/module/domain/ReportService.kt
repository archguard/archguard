package com.thoughtworks.archguard.module.domain

interface ReportService {
    fun getLogicModuleCouplingReport(): List<ModuleCouplingReportDTO>
    fun getLogicModuleCouplingReportDetail(): List<ModuleCouplingReport>
}