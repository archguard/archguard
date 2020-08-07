package com.thoughtworks.archguard.qualitygate.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProfileService {
    @Autowired
    private lateinit var repo: ProfileRepository

    fun getAll(): List<QualityGateProfile> {
        return repo.getAll()
    }

    fun create(profile: QualityGateProfile) {
        repo.create(profile)
    }

    fun update(id: String, profile: QualityGateProfile) {
        profile.id = id
        repo.update(profile)
    }

    fun delete(id: String) {
        repo.delete(id)
    }

}
