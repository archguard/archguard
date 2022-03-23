package com.thoughtworks.archguard.module.domain.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class PackageVOTest {
    @Test
    internal fun `should contain class`() {
        val packageVO = PackageVO("abc.def.ghi", "p1")
        val jClassVO1 = JClassVO.create("p1.abc.def.ghi.clazz1")
        val jClassVO2 = JClassVO.create("p1.abc.def.clazz1")
        val jClassVO3 = JClassVO.create("p1.abc.def.ghi.jkl.clazz2")
        val jClassVO4 = JClassVO.create("p1.abc.def.ghijkl.clazz3")
        val jClassVO5 = JClassVO.create("p1.xyz.abc.def.ghi.clazz3")

        assertThat(packageVO.containClass(jClassVO1)).isTrue()
        assertThat(packageVO.containClass(jClassVO2)).isFalse()
        assertThat(packageVO.containClass(jClassVO3)).isTrue()
        assertThat(packageVO.containClass(jClassVO4)).isFalse()
        assertThat(packageVO.containClass(jClassVO5)).isFalse()
    }

    @Test
    internal fun `should support init from from fullname`() {
        val packageVO = PackageVO.fromFullName("p1.abc.def.ghi")
        assertThat(packageVO.packageName).isEqualTo("abc.def.ghi")
        assertThat(packageVO.module).isEqualTo("p1")

        val packageVOWithEmptyPackage = PackageVO.fromFullName("p1")
        assertThat(packageVOWithEmptyPackage.packageName).isEqualTo("")
        assertThat(packageVOWithEmptyPackage.module).isEqualTo("p1")
    }

    @Test
    internal fun `should identify for dot dot`() {
        val packageVO = PackageVO.fromFullName("..")
        assertThat(packageVO.packageName).isEqualTo("")
        assertThat(packageVO.module).isEqualTo(".")
    }
}