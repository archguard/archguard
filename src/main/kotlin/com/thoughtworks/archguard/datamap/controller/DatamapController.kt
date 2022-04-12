package com.thoughtworks.archguard.datamap.controller

import com.thoughtworks.archguard.datamap.domain.Datamap
import com.thoughtworks.archguard.datamap.domain.DatamapService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/datamap")
class DatamapController(val service: DatamapService) {

    @GetMapping("/{systemId}")
    fun getThresholdsBySystemId(@PathVariable("systemId") systemId: Long): List<Datamap> {
        return service.getDatamapBySystemId(systemId)
    }
}




