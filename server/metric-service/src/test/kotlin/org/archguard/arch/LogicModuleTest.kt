package org.archguard.arch;

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class LogicModuleTest {

    @Test
    fun shouldHideLogicModule() {
        // Given
        val logicModule = LogicModule.create("1", "LogicModule", listOf(), listOf())

        // When
        logicModule.hide()

        // Then
        assertTrue(logicModule.status == LogicModuleStatus.HIDE)
    }

    @Test
    fun `should get class module by single full match`() {
        val lg1 = LogicModule("id1", "lg1",
            listOf(LeafManger.createLeaf("a"), LeafManger.createLeaf("a.b"), LeafManger.createLeaf("a.b.c.d")))
        val lg2 = LogicModule("id2", "lg2",
            listOf(LeafManger.createLeaf("a"), LeafManger.createLeaf("a.b"), LeafManger.createLeaf("a.b.c")))
        val logicModules: List<LogicModule> = listOf(lg1, lg2)
        val classModule = getModule(logicModules, LeafManger.createLeaf("a.b.c"))
        Assertions.assertThat(classModule).isEqualTo(listOf(lg2))
    }

    @Test
    fun `should get class module by multi full match`() {
        val lg1 = LogicModule("id1", "lg1",
            listOf(LeafManger.createLeaf("a"), LeafManger.createLeaf("a.b"), LeafManger.createLeaf("a.b.c.d")))
        val lg2 = LogicModule("id2", "lg2",
            listOf(LeafManger.createLeaf("a"), LeafManger.createLeaf("a.b"), LeafManger.createLeaf("a.b.c")))
        val lg3 = LogicModule("id3", "lg3",
            listOf(LeafManger.createLeaf("a"), LeafManger.createLeaf("a.b"), LeafManger.createLeaf("a.b.c")))
        val lg4 = LogicModule("id4", "lg4",
            listOf(LeafManger.createLeaf("a"), LeafManger.createLeaf("a.b.c"), LeafManger.createLeaf("a.b.c.d")))
        val logicModules: List<LogicModule> = listOf(lg1, lg2, lg3, lg4)
        val classModule = getModule(logicModules, LeafManger.createLeaf("a.b.c"))
        Assertions.assertThat(classModule).isEqualTo(listOf(lg2, lg3, lg4))
    }

    @Test
    fun `should get class module by single start with match`() {
        val lg1 = LogicModule("id1", "lg1",
            listOf(LeafManger.createLeaf("a"), LeafManger.createLeaf("a.b"), LeafManger.createLeaf("a.b.c.d")))
        val lg2 = LogicModule("id2", "lg2",
            listOf(LeafManger.createLeaf("a"), LeafManger.createLeaf("a.b"), LeafManger.createLeaf("abc")))
        val lg3 = LogicModule("id3", "lg3",
            listOf(
                LeafManger.createLeaf("a"),
                LeafManger.createLeaf("a.b"),
                LeafManger.createLeaf("abc.d.e.d.f"),
                LeafManger.createLeaf("abc.d.e.d")
            ))
        val lg4 = LogicModule("id4", "lg4",
            listOf(
                LeafManger.createLeaf("a"),
                LeafManger.createLeaf("a.b"),
                LeafManger.createLeaf("abc.d.e.d"),
                LeafManger.createLeaf("abc.d.e")
            ))
        val logicModules: List<LogicModule> = listOf(lg1, lg2, lg3, lg4)
        val classModule = getModule(logicModules, LeafManger.createLeaf("abc.d.e.d.f.g"))
        Assertions.assertThat(classModule).isEqualTo(listOf(lg3))
    }

    @Test
    fun `should get class module by multi start with match`() {
        val lg1 = LogicModule("id1", "lg1",
            listOf(LeafManger.createLeaf("a"), LeafManger.createLeaf("a.b"), LeafManger.createLeaf("a.b.c.d")))
        val lg2 = LogicModule("id2", "lg2",
            listOf(LeafManger.createLeaf("a"), LeafManger.createLeaf("a.b"), LeafManger.createLeaf("abc")))
        val lg3 = LogicModule("id3", "lg3",
            listOf(
                LeafManger.createLeaf("a"),
                LeafManger.createLeaf("a.b"),
                LeafManger.createLeaf("abc.d.e.d.f"),
                LeafManger.createLeaf("abc.d.e.d")
            ))
        val lg4 = LogicModule("id4", "lg4",
            listOf(
                LeafManger.createLeaf("a"),
                LeafManger.createLeaf("a.b"),
                LeafManger.createLeaf("abc.d.e.d"),
                LeafManger.createLeaf("abc.d.e")
            ))
        val lg5 = LogicModule("id5", "lg5",
            listOf(
                LeafManger.createLeaf("a"),
                LeafManger.createLeaf("a.b"),
                LeafManger.createLeaf("abc.d.e.d.f"),
                LeafManger.createLeaf("abc.d.e")
            ))
        val lg6 = LogicModule("id6", "lg6",
            listOf(
                LeafManger.createLeaf("a"),
                LeafManger.createLeaf("a.b"),
                LeafManger.createLeaf("abc.d.e.d.f.g.h"),
                LeafManger.createLeaf("abc.d.e")
            ))
        val logicModules: List<LogicModule> = listOf(lg1, lg2, lg3, lg4, lg5, lg6)
        val classModule = getModule(logicModules, LeafManger.createLeaf("abc.d.e.d.f.g"))
        Assertions.assertThat(classModule).isEqualTo(listOf(lg3, lg5))
    }

}