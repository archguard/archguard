package org.archguard.trace.storage

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import org.archguard.trace.model.TraceRecord
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

private val logger = KotlinLogging.logger {}

/**
 * Database table for Agent Trace records
 */
object TraceRecordsTable : LongIdTable("agent_trace_records") {
    val traceId = varchar("trace_id", 255).uniqueIndex()
    val version = varchar("version", 20)
    val timestamp = timestamp("timestamp")
    val vcsType = varchar("vcs_type", 20)
    val vcsRevision = varchar("vcs_revision", 255)
    val vcsRepository = varchar("vcs_repository", 500).nullable()
    val vcsBranch = varchar("vcs_branch", 255).nullable()
    val toolName = varchar("tool_name", 100)
    val toolVersion = varchar("tool_version", 50).nullable()
    val recordJson = text("record_json") // Full JSON for flexibility
    val createdAt = timestamp("created_at").default(Instant.now())
}

/**
 * Database-backed Trace Storage using Exposed ORM
 * 
 * Supports both H2 (development/testing) and PostgreSQL (production).
 */
class DatabaseTraceStorage(
    private val database: Database
) : TraceStorage {
    
    private val json = Json {
        prettyPrint = false
        ignoreUnknownKeys = true
    }
    
    init {
        // Create tables if not exist
        transaction(database) {
            SchemaUtils.create(TraceRecordsTable)
        }
        logger.info { "Database trace storage initialized" }
    }
    
    override suspend fun store(record: TraceRecord) {
        transaction(database) {
            val existingId = TraceRecordsTable.select {
                TraceRecordsTable.traceId eq record.id
            }.singleOrNull()?.get(TraceRecordsTable.id)
            
            if (existingId != null) {
                // Update existing record
                TraceRecordsTable.update({ TraceRecordsTable.id eq existingId }) {
                    it[version] = record.version
                    it[timestamp] = Instant.parse(record.timestamp)
                    it[vcsType] = record.vcs.type
                    it[vcsRevision] = record.vcs.revision
                    it[vcsRepository] = record.vcs.repository
                    it[vcsBranch] = record.vcs.branch
                    it[toolName] = record.tool.name
                    it[toolVersion] = record.tool.version
                    it[recordJson] = json.encodeToString(record)
                }
                logger.debug { "Updated trace record: ${record.id}" }
            } else {
                // Insert new record
                TraceRecordsTable.insert {
                    it[traceId] = record.id
                    it[version] = record.version
                    it[timestamp] = Instant.parse(record.timestamp)
                    it[vcsType] = record.vcs.type
                    it[vcsRevision] = record.vcs.revision
                    it[vcsRepository] = record.vcs.repository
                    it[vcsBranch] = record.vcs.branch
                    it[toolName] = record.tool.name
                    it[toolVersion] = record.tool.version
                    it[recordJson] = json.encodeToString(record)
                }
                logger.debug { "Inserted trace record: ${record.id}" }
            }
        }
    }
    
    override suspend fun get(id: String): TraceRecord? {
        return transaction(database) {
            TraceRecordsTable.select {
                TraceRecordsTable.traceId eq id
            }.singleOrNull()?.let { row ->
                json.decodeFromString<TraceRecord>(row[TraceRecordsTable.recordJson])
            }
        }
    }
    
    override suspend fun list(offset: Int, limit: Int): List<TraceRecord> {
        return transaction(database) {
            TraceRecordsTable
                .selectAll()
                .orderBy(TraceRecordsTable.timestamp, SortOrder.DESC)
                .limit(limit, offset.toLong())
                .map { row ->
                    json.decodeFromString<TraceRecord>(row[TraceRecordsTable.recordJson])
                }
        }
    }
    
    override fun count(): Long {
        return transaction(database) {
            TraceRecordsTable.selectAll().count()
        }
    }
    
    override suspend fun delete(id: String): Boolean {
        return transaction(database) {
            TraceRecordsTable.deleteWhere {
                traceId eq id
            } > 0
        }
    }
    
    override fun type(): String = "database"
    
    /**
     * Search traces by VCS revision
     */
    fun findByRevision(revision: String, offset: Int = 0, limit: Int = 100): List<TraceRecord> {
        return transaction(database) {
            TraceRecordsTable.select {
                TraceRecordsTable.vcsRevision eq revision
            }
                .orderBy(TraceRecordsTable.timestamp, SortOrder.DESC)
                .limit(limit, offset.toLong())
                .map { row ->
                    json.decodeFromString<TraceRecord>(row[TraceRecordsTable.recordJson])
                }
        }
    }
    
    /**
     * Search traces by tool name
     */
    fun findByTool(toolName: String, offset: Int = 0, limit: Int = 100): List<TraceRecord> {
        return transaction(database) {
            TraceRecordsTable.select {
                TraceRecordsTable.toolName eq toolName
            }
                .orderBy(TraceRecordsTable.timestamp, SortOrder.DESC)
                .limit(limit, offset.toLong())
                .map { row ->
                    json.decodeFromString<TraceRecord>(row[TraceRecordsTable.recordJson])
                }
        }
    }
    
    /**
     * Search traces by time range
     */
    fun findByTimeRange(
        startTime: Instant,
        endTime: Instant,
        offset: Int = 0,
        limit: Int = 100
    ): List<TraceRecord> {
        return transaction(database) {
            TraceRecordsTable.select {
                (TraceRecordsTable.timestamp greaterEq startTime) and
                (TraceRecordsTable.timestamp lessEq endTime)
            }
                .orderBy(TraceRecordsTable.timestamp, SortOrder.DESC)
                .limit(limit, offset.toLong())
                .map { row ->
                    json.decodeFromString<TraceRecord>(row[TraceRecordsTable.recordJson])
                }
        }
    }
    
    /**
     * Clear all records (for testing)
     */
    fun clear() {
        transaction(database) {
            TraceRecordsTable.deleteAll()
        }
        logger.debug { "Cleared all trace records" }
    }
}

/**
 * Database configuration
 */
data class DatabaseConfig(
    val url: String = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
    val driver: String = "org.h2.Driver",
    val user: String = "sa",
    val password: String = ""
)

/**
 * Create database connection
 */
fun createDatabase(config: DatabaseConfig = DatabaseConfig()): Database {
    return Database.connect(
        url = config.url,
        driver = config.driver,
        user = config.user,
        password = config.password
    )
}
