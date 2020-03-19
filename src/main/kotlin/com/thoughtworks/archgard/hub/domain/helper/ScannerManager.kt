package com.thoughtworks.archgard.hub.domain.helper

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.Scanner
import org.springframework.beans.factory.annotation.Autowired

class ScannerManager {
    private val scanners: ArrayList<Scanner> = ArrayList()

    @Autowired
    lateinit var badSmellScanner: Scanner

    fun load() {
        scanners.add(badSmellScanner)
    }

    fun execute(context: ScanContext) {
        scanners.forEach {
            it.scan(context)
        }
    }
}