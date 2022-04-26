package org.archguard.rule.api

import org.archguard.rule.core.RuleDecl
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.reflections.Reflections
import org.reflections.util.ConfigurationBuilder
import kotlin.reflect.KClass

import org.reflections.scanners.Scanners.TypesAnnotated

internal class AnnotationUtilsTest {
    @Test
    @Disabled("Because scanner is cross multiple modules")
    internal fun setUp() {
        val listClass: KClass<RuleDecl> = RuleDecl::class
        val reflections = Reflections(
            ConfigurationBuilder()
                .forPackage("org.archguard.rule")
                .setScanners(TypesAnnotated))

        val annotated: Set<Class<*>> = reflections.getTypesAnnotatedWith(RuleDecl::class.java)

        println(annotated)
    }
}