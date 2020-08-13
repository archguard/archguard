package com.thoughtworks.archguard.qualitygate.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProfileService {
    @Autowired
    private lateinit var repo: ProfileRepository

    fun getByNameOrDefault(name: String): QualityGateProfile {
        val profile = repo.getByName(name)

        return profile ?: QualityGateProfile.default()
    }

    fun getAll(): List<QualityGateProfile> {
        return repo.getAll()
    }

    fun create(profile: QualityGateProfile) {
        repo.create(profile)
    }

    fun update(id: Long, profile: QualityGateProfile) {
        profile.id = id
        repo.update(profile)
    }

    fun delete(id: Long) {
        repo.delete(id)
    }

}
