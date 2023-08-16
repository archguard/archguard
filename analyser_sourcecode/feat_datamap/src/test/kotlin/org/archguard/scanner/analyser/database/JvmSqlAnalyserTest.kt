package org.archguard.scanner.analyser.database

import io.kotest.matchers.shouldBe
import org.archguard.scanner.analyser.xml.mybatis.MybatisEntry
import org.junit.jupiter.api.Test

class JvmSqlAnalyserTest {
    @Test
    fun should_convert_entity() {
        val analyser = JvmSqlAnalyser()
        val entires: List<MybatisEntry> = listOf(
            MybatisEntry(
                "org.apache.ibatis.domain.blog.mappers.AuthorMapper",
                mutableMapOf(
                    "selectAuthor" to "select * from author where id = #{id}",
                ),
            )
        )
        val result = analyser.convertMyBatis(entires)

        result.size shouldBe 1
        val relation = result[0]
        relation.sqls[0] shouldBe "select * from author where id = #{id}"
        relation.packageName shouldBe "org.apache.ibatis.domain.blog.mappers"
        relation.className shouldBe "AuthorMapper"
        relation.functionName shouldBe "selectAuthor"
    }
}