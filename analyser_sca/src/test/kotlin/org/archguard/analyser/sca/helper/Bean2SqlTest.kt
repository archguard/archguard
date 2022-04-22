package org.archguard.analyser.sca.helper

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Bean2SqlTest {
    @Test
    fun split_when_bean_sql() {
        val samples: MutableList<CompositionDependency> = mutableListOf()
        for (i in 0..150) {
            samples += CompositionDependency(i.toString(), "", "", "", "", "", "", "", "", "")
        }

        val bean2Sql = Bean2Sql()
        val sqls = bean2Sql.bean2Sql(samples)

        var splits = sqls.split("\n")
        assertEquals(1, splits.size)
    }
}