package com.thoughtworks.archguard.qualitygate.infrastructure

import com.thoughtworks.archguard.qualitygate.domain.ProfileRepository
import com.thoughtworks.archguard.qualitygate.domain.QualityGateProfile
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.jdbi.v3.sqlobject.transaction.Transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Repository
class ProfileRepositoryImpl(@Autowired val profileDao: ProfileDao) : ProfileRepository {
    override fun getAll(): List<QualityGateProfile> {
        return profileDao.findAll().map { it.toProfile() }
    }

    override fun update(profile: QualityGateProfile) {
        profile.updatedAt = LocalDateTime.now()
        profileDao.update(profile.toDto())
    }

    override fun create(profile: QualityGateProfile) {
        profileDao.insert(profile.toDto())
    }

    override fun delete(id: String) {
        profileDao.delete(id)
    }
}