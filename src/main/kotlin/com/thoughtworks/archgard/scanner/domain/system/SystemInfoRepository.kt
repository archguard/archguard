package com.thoughtworks.archgard.scanner.domain.system

interface SystemInfoRepository {

    fun getSystemInfo(id: Long): SystemInfo?
}
