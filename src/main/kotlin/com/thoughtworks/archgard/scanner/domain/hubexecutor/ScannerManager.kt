package com.thoughtworks.archgard.scanner.domain.hubexecutor

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.Scanner
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ScannerManager(@Autowired private val scanners: List<Scanner>) {
    fun execute(context: ScanContext) {
        scanners.forEach {
            it.scan(context)
        }
    }
}