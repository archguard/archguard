package com.thoughtworks.archguard.scanner2.appl

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class NameUtilKtTest {

    @Test
    fun getModuleNameFromPackageFullName() {
        val moduleName = getModuleNameFromPackageFullName(".com.qicaisheng.parkinglot")
        assertEquals(".", moduleName)
    }

    @Test
    fun getPackageNameFromPackageFullName() {
        val packageName = getPackageNameFromPackageFullName(".com.qicaisheng.parkinglot")
        assertEquals("com.qicaisheng.parkinglot", packageName)
    }
}