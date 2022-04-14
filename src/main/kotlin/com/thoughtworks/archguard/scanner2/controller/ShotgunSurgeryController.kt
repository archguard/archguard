package com.thoughtworks.archguard.scanner2.controller

import com.thoughtworks.archguard.scanner2.domain.service.ScannerShotgunSurgeryService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable

@Controller
class ShotgunSurgeryController(val scannerShotgunSurgeryService: ScannerShotgunSurgeryService) {

    fun persist(@PathVariable("systemId") systemId: Long) {
        return scannerShotgunSurgeryService.persist(systemId)
    }
}
