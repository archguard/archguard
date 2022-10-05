package com.thoughtworks.archguard.systeminfo.domain

interface SystemInfoRepository {
    fun getById(id: Long): SystemInfo?

    fun getSystemInfoList(): List<SystemInfo>

    fun updateSystemInfo(systemInfo: SystemInfo): Int

    fun addSystemInfo(systemInfo: SystemInfo): Long

    fun queryBySystemName(systemName: String): Int

    fun deleteSystemInfo(id: Long): Int

    fun deleteSystemInfoRelated()

    fun getByName(name: String): SystemInfo?
}
