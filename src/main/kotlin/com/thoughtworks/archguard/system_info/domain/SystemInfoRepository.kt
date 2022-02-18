package com.thoughtworks.archguard.system_info.domain


interface SystemInfoRepository {
    fun getSystemInfo(id: Long): SystemInfo?

    fun getSystemInfoList(): List<SystemInfo>

    fun updateSystemInfo(systemInfo: SystemInfo): Int

    fun addSystemInfo(systemInfo: SystemInfo): Long

    fun queryBysystemName(systemName: String): Int

    fun deleteSystemInfo(id: Long): Int

    fun deleteSystemInfoRelated()
}
