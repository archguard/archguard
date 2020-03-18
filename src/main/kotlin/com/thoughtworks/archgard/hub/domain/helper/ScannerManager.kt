package com.thoughtworks.archgard.hub.domain.helper

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.toolscanners.ScannerLifecycle
import org.springframework.beans.factory.annotation.Autowired

class ScannerManager {
    private val scanners: ArrayList<ScannerLifecycle> = ArrayList()

    @Autowired
    lateinit var checkStyleScanner: ScannerLifecycle

    @Autowired
    lateinit var cocaScanner: ScannerLifecycle

    @Autowired
    lateinit var gitScanner: ScannerLifecycle

    @Autowired
    lateinit var jacocoScanner: ScannerLifecycle

    @Autowired
    lateinit var javaDependencyScanner: ScannerLifecycle

    fun load() {
        scanners.add(checkStyleScanner)
        scanners.add(cocaScanner)
        scanners.add(gitScanner)
        scanners.add(jacocoScanner)
        scanners.add(javaDependencyScanner)
    }

    fun execute(context: ScanContext) {
        scanners.forEach {
            it.preProcess(context)
            it.scan(context)
            it.storeReport(context)
            it.clean(context)
        }
    }
}