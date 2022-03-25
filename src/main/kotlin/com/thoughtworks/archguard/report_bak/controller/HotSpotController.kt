package com.thoughtworks.archguard.report_bak.controller

import com.thoughtworks.archguard.report_bak.infrastructure.HotSpotRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class HotSpotController(@Autowired  private  val hotSpotRepo: HotSpotRepo) {

    @GetMapping("/hotspot")
    fun top(@RequestParam("top", defaultValue = "5") top: Int): List<HotSpotOut> {
        return hotSpotRepo.queryHotSpotPath(top).map {
            HotSpotOut(it.first, it.second)
        }
    }

    class HotSpotOut(val path: String, val count: Int)
}