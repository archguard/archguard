package com.thoughtworks.archguard.scanner2.controller

import com.thoughtworks.archguard.scanner2.domain.service.ShotgunSurgeryService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable

@Controller
class ShotgunSurgeryController(val shotgunSurgeryService: ShotgunSurgeryService) {

    fun persist(@PathVariable("systemId") systemId: Long) {
        return shotgunSurgeryService.persist(systemId)
    }
}