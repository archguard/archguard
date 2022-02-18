package com.thoughtworks.archguard.report_bak.domain.repository

interface CheckStyleRepository {

    fun getCheckStyleOverview(): List<String>
}
