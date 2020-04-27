package com.thoughtworks.archguard.report.infrastructure

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class TestBadSmellRepo(@Autowired private val jdbi: Jdbi) {

    fun getTestBadSmellCount(): List<TestBadSmellCountDBO> {
        return jdbi.withHandle<List<TestBadSmellCountDBO>, Nothing> { handle ->
            handle.registerRowMapper(ConstructorMapper.factory(TestBadSmellCountDBO::class.java))
            handle.createQuery("select type, count(id) as size from testBadSmell group by type")
                    .mapTo(TestBadSmellCountDBO::class.java)
                    .list()
        }
    }

    fun getTotalTestCount(): Int {
        return jdbi.withHandle<Int, RuntimeException> { handle: Handle ->
            handle.createQuery("select overview_value from Overview where overview_type='test'")
                    .mapTo(Int::class.java).firstOrNull()
        }
    }

    fun getTestBadSmellByTest(fileNames: List<String>): List<TestBadSmellCountDBO> {
        return jdbi.withHandle<List<TestBadSmellCountDBO>, Nothing> { handle ->
            handle.registerRowMapper(ConstructorMapper.factory(TestBadSmellCountDBO::class.java))
            handle.createQuery("select type, count(id) as size from testBadSmell " +
                    "where file_name in ( ${fileNames.joinToString("','", "'", "'")} )" +
                    "group by type ")
                    .mapTo(TestBadSmellCountDBO::class.java)
                    .list()
        }
    }
}