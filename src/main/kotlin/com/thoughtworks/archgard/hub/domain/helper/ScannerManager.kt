package com.thoughtworks.archgard.hub.domain.helper

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.Scanner
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ScannerManager {
    private val scanners: ArrayList<Scanner> = ArrayList()

    @Autowired
    lateinit var badSmellScanner: Scanner
    lateinit var testBadSmellScanner: Scanner

    fun load() {
        scanners.add(badSmellScanner)
        scanners.add(testBadSmellScanner)
    }

    fun execute(context: ScanContext) {
        scanners.forEach {
            it.scan(context)
        }
    }
}