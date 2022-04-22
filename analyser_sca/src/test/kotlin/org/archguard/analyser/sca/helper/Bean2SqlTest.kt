package org.archguard.analyser.sca.helper

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class Bean2SqlTest {
    @Test
    fun normal_bean() {
        val samples: MutableList<CompositionDependency> = mutableListOf()
        samples += CompositionDependency("12", "", "", "", "", "", "", "", "", "", "")

        val bean2Sql = Bean2Sql()
        val sqls = bean2Sql.bean2Sql(samples)

        val splits = sqls.split("\n")
        assertEquals(1, splits.size)
        assertEquals("insert into project_composition_dependencies(dep_artifact, dep_group, dep_metadata, dep_source, dep_name, id, name, package_manager, parent_id, system_id, version) values ('', '', '', '', '', '12', '', '', '', '', '');",
            sqls)
    }

    @Test
    fun two_beans() {
        val samples: MutableList<CompositionDependency> = mutableListOf()
        samples += CompositionDependency("1", "", "", "", "", "", "", "", "", "", "")
        samples += CompositionDependency("2", "", "", "", "", "", "", "", "", "", "")

        val bean2Sql = Bean2Sql()
        val sqls = bean2Sql.bean2Sql(samples)

        val splits = sqls.split("\n")
        assertEquals(1, splits.size)
        assertEquals("insert into project_composition_dependencies(dep_artifact, dep_group, dep_metadata, dep_source, dep_name, id, name, package_manager, parent_id, system_id, version) values ('', '', '', '', '', '1', '', '', '', '', ''),('', '', '', '', '', '2', '', '', '', '', '');",
            sqls)
    }

    @Test
    fun split_when_beans_more_than_100() {
        val samples: MutableList<CompositionDependency> = mutableListOf()
        for (i in 0..150) {
            samples += CompositionDependency(i.toString(), "", "", "", "", "", "", "", "", "", "")
        }

        val bean2Sql = Bean2Sql()
        val sqls = bean2Sql.bean2Sql(samples)

        val splits = sqls.split("\n")
        assertEquals(2, splits.size)
    }
}