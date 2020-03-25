package com.thoughtworks.archguard.report.domain.repository

interface CheckStyleRepository {

    fun getCheckStyleOverview(): List<String>
}
