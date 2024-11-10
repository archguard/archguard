package org.archguard.scanner.analyser.database

import chapi.ast.goast.GoAnalyser
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class GoSqlAnalyserTest {
    @Test
    fun should_identify_item() {
        val codeFile = javaClass.classLoader.getResource("go_database/subject.go").file
        val code = File(codeFile).readText()
        val ds = GoAnalyser().analysis(code, codeFile).DataStructures

        val analyser = GoSqlAnalyser()
        val result = ds.map {
            analyser.analysisByNode(it, "")
        }

        result.size shouldBe 1
    }

    @Test
    fun should_identify_query() {
        val codeFile = javaClass.classLoader.getResource("go_database/task.go").file
        val code = File(codeFile).readText()
        val ds = GoAnalyser().analysis(code, codeFile).DataStructures

        val analyser = GoSqlAnalyser()
        val result = ds.map {
            analyser.analysisByNode(it, "")
        }.flatten()

        println(result.joinToString("\n") { it.sqls.joinToString("\n") })
        result.size shouldBe 18

        assertEquals(
            result[0].sqls[0],
            "SELECT id,business_id,flow_id,rid,admin_id,uid,state,weight,utime,gtime,mid,fans,`group`,reason,ctime,mtime from task WHERE id=?"
        )
        assertEquals(
            result[1].sqls[0],
            "SELECT gtime FROM task WHERE id=? AND state=? AND uid=?"
        )
    }
}
