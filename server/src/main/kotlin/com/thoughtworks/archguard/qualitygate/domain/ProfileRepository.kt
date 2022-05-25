package com.thoughtworks.archguard.qualitygate.domain

interface ProfileRepository {
    fun getByName(name: String): QualityGateProfile?
    fun getAll(): List<QualityGateProfile>
    fun create(profile: QualityGateProfile)
    fun update(profile: QualityGateProfile)
    fun delete(id: Long)
}
