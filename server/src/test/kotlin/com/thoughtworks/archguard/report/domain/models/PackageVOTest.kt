package com.thoughtworks.archguard.report.domain.models;

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PackageVOTest {

    @Test
    fun shouldCreatePackageVO() {
        // given
        val fullName = "com.thoughtworks.archguard.report.domain.models.PackageVO"

        // when
        val packageVO = PackageVO.create(fullName)

        // then
        assertEquals("com", packageVO.moduleName)
        assertEquals("thoughtworks.archguard.report.domain.models.PackageVO", packageVO.packageName)
    }

    @Test
    fun shouldCreatePackageVOWithSingleLevelPackage() {
        // given
        val fullName = "com.PackageVO"

        // when
        val packageVO = PackageVO.create(fullName)

        // then
        assertEquals("com", packageVO.moduleName)
        assertEquals("PackageVO", packageVO.packageName)
    }

    @Test
    fun shouldCreatePackageVOWithEmptyPackageName() {
        // given
        val fullName = "com."

        // when
        val packageVO = PackageVO.create(fullName)

        // then
        assertEquals("com", packageVO.moduleName)
        assertEquals("", packageVO.packageName)
    }
}
