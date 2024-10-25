package com.thoughtworks.archguard.smartscanner.repository

import org.archguard.context.CompositionDependency
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.SqlBatch
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class ScaRepository(private val jdbi: Jdbi) {
    private val log = LoggerFactory.getLogger(this.javaClass)
    private val dao: ScaDao by lazy { jdbi.onDemand(ScaDao::class.java) }

    fun saveDependencies(systemId: Long, input: List<CompositionDependency>) {
        log.debug("clean up old data for systemId: $systemId")
        dao.deleteBySystemId(systemId)

        log.debug("save new data for systemId: $systemId")
        dao.saveAll(systemId, input)

        log.debug("save new data for systemId: $systemId done")
    }
}

interface ScaDao {
    @SqlBatch(
        """
        INSERT INTO project_composition_dependencies (system_id,id,name,version,parent_id,package_manager,dep_name,dep_group,dep_artifact,dep_metadata,dep_source,dep_scope,dep_version)
        VALUES (:systemId,:item.id,:item.name,:item.version,:item.parentId,:item.packageManager,:item.depName,:item.depGroup,:item.depArtifact,:item.depMetadata,:item.depSource,:item.depScope,:item.depVersion)
        """
    )
    fun saveAll(systemId: Long, @BindBean("item") commitLogs: List<CompositionDependency>)

    @SqlUpdate("DELETE FROM project_composition_dependencies WHERE system_id = :systemId")
    fun deleteBySystemId(systemId: Long)
}
