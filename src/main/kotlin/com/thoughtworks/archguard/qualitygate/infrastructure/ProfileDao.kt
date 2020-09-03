package com.thoughtworks.archguard.qualitygate.infrastructure

import com.thoughtworks.archguard.qualitygate.domain.QualityGateProfile
import com.thoughtworks.archguard.qualitygate.domain.QualityGateProfileDTO
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper
import org.jdbi.v3.sqlobject.config.RegisterColumnMapper
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

@RegisterBeanMapper(QualityGateProfileDTO::class)
interface ProfileDao {
    @SqlUpdate("INSERT INTO quality_gate_profile (name, config) VALUES (:name, :config)")
    fun insert(@BindBean profile: QualityGateProfile)

    @SqlQuery("SELECT * FROM quality_gate_profile WHERE name=:name")
    @RegisterColumnMapper(DateMapper::class)
    fun findByName(@Bind("name") name: String): QualityGateProfile?

    @SqlQuery("SELECT count(1) FROM quality_gate_profile WHERE name=:name")
    fun countByName(@Bind("name") name: String): Int

    @SqlQuery("SELECT * FROM quality_gate_profile")
    @RegisterColumnMapper(DateMapper::class)
    fun findAll(): List<QualityGateProfile>

    @SqlUpdate("UPDATE quality_gate_profile SET name=:name, config=:config WHERE id=:id")
    fun update(@BindBean profile: QualityGateProfile)

    @SqlUpdate("DELETE FROM quality_gate_profile WHERE id=:id")
    fun delete(@Bind("id") id: Long)
}
