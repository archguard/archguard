package com.thoughtworks.archguard.report_bak.infrastructure

import com.thoughtworks.archguard.evaluation_bak.domain.TestBadSmellCount
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class TestBadSmellRepo(@Autowired private val jdbi: Jdbi) {

    fun getTestBadSmellCount(): List<TestBadSmellCount> {
        return jdbi.withHandle<List<TestBadSmellCountDBO>, Nothing> { handle ->
            handle.registerRowMapper(ConstructorMapper.factory(TestBadSmellCountDBO::class.java))
            handle.createQuery("select type, count(id) as size from testBadSmell group by type")
                    .mapTo(TestBadSmellCountDBO::class.java)
                    .list()
        }
                .map { TestBadSmellCount(it.type, it.size) }
    }

    fun getTotalTestCount(): Int {
        return jdbi.withHandle<Int, RuntimeException> { handle: Handle ->
            handle.createQuery("select overview_value from system_overview where overview_type='test'")
                    .mapTo(Int::class.java).findOne().orElse(0)
        }
    }

    fun getTestBadSmellByTest(fileNames: List<String>): List<TestBadSmellCount> {
        return jdbi.withHandle<List<TestBadSmellCountDBO>, Nothing> { handle ->
            handle.registerRowMapper(ConstructorMapper.factory(TestBadSmellCountDBO::class.java))
            handle.createQuery("select type, count(id) as size from testBadSmell " +
                    "where file_name in ( ${fileNames.joinToString("','", "'", "'")} )" +
                    "group by type ")
                    .mapTo(TestBadSmellCountDBO::class.java)
                    .list()
        }
                .map { TestBadSmellCount(it.type, it.size) }
    }
}