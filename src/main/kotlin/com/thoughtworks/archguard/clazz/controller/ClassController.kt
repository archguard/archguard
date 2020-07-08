package com.thoughtworks.archguard.clazz.controller

import com.thoughtworks.archguard.clazz.domain.service.ClassService
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClass
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/classes")
class ClassController {

    @Autowired
    private lateinit var service: ClassService

    @GetMapping("/{name}")
    fun getDependencies(@PathVariable("name") name: String,
                        @RequestParam("deep") deep: Int = 4): Dependency<List<JClass>> {
        return service.findDependencies(name, deep)
    }
}