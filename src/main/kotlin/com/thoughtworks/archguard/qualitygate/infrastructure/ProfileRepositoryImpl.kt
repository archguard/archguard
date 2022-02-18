package com.thoughtworks.archguard.qualitygate.infrastructure

import com.thoughtworks.archguard.common.exception.DuplicateResourceException
import com.thoughtworks.archguard.qualitygate.domain.ProfileRepository
import com.thoughtworks.archguard.qualitygate.domain.QualityGateProfile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class ProfileRepositoryImpl(@Autowired val profileDao: ProfileDao) : ProfileRepository {
    override fun getByName(name: String): QualityGateProfile? {
        return profileDao.findByName(name)
    }

    override fun getAll(): List<QualityGateProfile> {
        return profileDao.findAll()
    }

    override fun update(profile: QualityGateProfile) {
        profileDao.update(profile)
    }

    override fun create(profile: QualityGateProfile) {
        if (isNameExist(profile.name)) {
            throw DuplicateResourceException("Quality Gate Profile Name \"${profile.name}\" Duplicate")
        }
        profileDao.insert(profile)
    }

    override fun delete(id: Long) {
        profileDao.delete(id)
    }

    fun isNameExist(name: String): Boolean {
        return profileDao.countByName(name) > 0
    }
}
