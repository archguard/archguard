package com.thoughtworks.archguard.qualitygate.infrastructure

import com.thoughtworks.archguard.qualitygate.domain.QualityGateProfileDTO
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

@RegisterBeanMapper(QualityGateProfileDTO::class)
interface ProfileDao {
    @SqlUpdate("INSERT INTO quality_gate_profile (id, name, config, createdAt, updatedAt) VALUES (:id, :name, :config, :createdAt, :updatedAt)")
    fun insert(@BindBean profile: QualityGateProfileDTO)

    @SqlQuery("SELECT * FROM quality_gate_profile")
    fun findAll(): List<QualityGateProfileDTO>

    @SqlUpdate("UPDATE quality_gate_profile SET name=:name, config=:config, updatedAt=:updatedAt WHERE id=:id")
    fun update(@BindBean profile: QualityGateProfileDTO)

    @SqlUpdate("DELETE FROM quality_gate_profile WHERE id=:id")
    fun delete(@Bind("id") id: String)
}
