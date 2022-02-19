package com.thoughtworks.archguard.scanner2.domain.service

import com.thoughtworks.archguard.scanner2.domain.model.Edge
import com.thoughtworks.archguard.scanner2.domain.model.JClass
import com.thoughtworks.archguard.scanner2.domain.model.JField
import com.thoughtworks.archguard.scanner2.domain.model.JMethod
import com.thoughtworks.archguard.scanner2.domain.repository.JClassRepository
import com.thoughtworks.archguard.scanner2.domain.repository.JMethodRepository
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class LCOM4ServiceTest {
    private lateinit var lcom4Service: LCOM4Service

    @MockK
    private lateinit var jClassRepository: JClassRepository

    @MockK
    private lateinit var jMethodRepo: JMethodRepository

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)
        lcom4Service = LCOM4Service(jClassRepository, jMethodRepo)
    }

    @Test
    internal fun should_get_lcom4_graph() {
        val jClass = JClass("id1", "clazz1", "module1")
        val jField1 = JField("f1", "f1", "String")
        val jField2 = JField("f2", "f2", "Int")
        jClass.fields = listOf(jField1, jField2)
        val jMethod1 = JMethod("m1", "m1", "clazz1", "module1", "void", emptyList())
        jMethod1.fields = listOf(jField1)
        val jMethod2 = JMethod("m2", "m2", "clazz1", "module1", "String", emptyList())
        jMethod2.fields = listOf(jField2)
        val jMethod3 = JMethod("m3", "m3", "clazz1", "module1", "Boolean", emptyList())
        val jMethod4 = JMethod("m4", "m4", "clazz2", "module2", "Boolean", emptyList())
        jMethod3.callees = listOf(jMethod2, jMethod4)
        jClass.methods = listOf(jMethod1, jMethod2, jMethod3)
        val lcom4Graph = lcom4Service.getLCOM4Graph(jClass)

        assertThat(lcom4Graph.toDirectedGraph().nodes.size).isEqualTo(5)
        assertThat(lcom4Graph.toDirectedGraph().edges.size).isEqualTo(3)
        assertThat(lcom4Graph.toDirectedGraph().edges).contains(Edge("m1", "f1", 1), Edge("m2", "f2", 1), Edge("m3", "m2", 1))
    }
}