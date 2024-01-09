package org.archguard.operator;

import org.archguard.model.code.JClass
import org.archguard.model.code.JField
import org.archguard.model.code.JMethod
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test;

internal class DataClassCheckerTest {

    @Test
    internal fun should_return_true_when_class_is_data_class() {
        val jClass = JClass("c1", "class1", "module1")
        val jField1 = JField("j1", "hello", "boolean")
        val jField2 = JField("j2", "goodField", "java.lang.String")
        jClass.fields = listOf(jField1, jField2)
        val jMethod1 = JMethod("m1", "isHello", "class1", "module1", "boolean", emptyList())
        val jMethod2 = JMethod("m2", "setHello", "class1", "module1", "void", listOf("boolean"))
        val jMethod3 = JMethod("m3", "getGoodField", "class1", "module1", "java.lang.String", emptyList())
        val jMethod4 = JMethod("m4", "setGoodField", "class1", "module1", "void", listOf("java.lang.String"))
        jClass.methods = listOf(jMethod1, jMethod2, jMethod3, jMethod4)

        Assertions.assertThat(DataClassChecker.check(jClass)).isTrue()
    }

    @Test
    internal fun should_return_false_when_check_class_is_data_class_when_wrong_method_count() {
        val jClass = JClass("c1", "class1", "module1")
        val jField1 = JField("j1", "hello", "boolean")
        val jField2 = JField("j2", "goodField", "java.lang.String")
        jClass.fields = listOf(jField1, jField2)
        val jMethod1 = JMethod("m1", "isHello", "class1", "module1", "boolean", emptyList())
        val jMethod2 = JMethod("m2", "setHello", "class1", "module1", "void", listOf("boolean"))
        val jMethod3 = JMethod("m3", "getGoodField", "class1", "module1", "java.lang.String", emptyList())
        val jMethod4 = JMethod("m4", "setGoodField", "class1", "module1", "void", listOf("java.lang.String"))
        val jMethod5 = JMethod("m5", "check", "class1", "module1", "boolean", listOf("java.lang.String"))

        jClass.methods = listOf(jMethod1, jMethod2, jMethod3, jMethod4, jMethod5)

        Assertions.assertThat(DataClassChecker.check(jClass)).isFalse()
    }

    @Test
    internal fun should_return_false_when_check_class_is_not_data_class_when_wrong_name() {
        val jClass = JClass("c1", "class1", "module1")
        val jField1 = JField("j1", "hello", "boolean")
        val jField2 = JField("j2", "goodField", "java.lang.String")
        jClass.fields = listOf(jField1, jField2)
        val jMethod1 = JMethod("m1", "isHello", "class1", "module1", "boolean", emptyList())
        val jMethod2 = JMethod("m2", "setHello", "class1", "module1", "void", listOf("boolean"))
        val jMethod3 = JMethod("m3", "retrieveGoodField", "class1", "module1", "java.lang.String", emptyList())
        val jMethod4 = JMethod("m4", "setGoodField", "class1", "module1", "void", listOf("java.lang.String"))

        jClass.methods = listOf(jMethod1, jMethod2, jMethod3, jMethod4)

        Assertions.assertThat(DataClassChecker.check(jClass)).isFalse()
    }
}