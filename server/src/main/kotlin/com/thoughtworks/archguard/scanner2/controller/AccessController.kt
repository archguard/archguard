package com.thoughtworks.archguard.scanner2.controller

import com.thoughtworks.archguard.scanner2.domain.service.AccessService
import org.springframework.stereotype.Controller

@Controller
class AccessController(val accessService: AccessService) {

    fun persist(systemId: Long) {
        return accessService.persist(systemId)
    }
}
