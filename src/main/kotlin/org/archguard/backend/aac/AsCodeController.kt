package org.archguard.backend.aac

import com.thoughtworks.archguard.system_info.controller.SystemInfoDTO
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/ascode")
class AsCodeController {
    private val logger = LoggerFactory.getLogger(AsCodeController::class.java)

    @PostMapping
    fun createRepos(@RequestBody systemInfoDTO: List<SystemInfoDTO>) {
        // todo:
    }
}
