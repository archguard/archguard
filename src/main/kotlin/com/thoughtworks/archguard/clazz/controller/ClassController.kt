package com.thoughtworks.archguard.clazz.controller

import com.thoughtworks.archguard.clazz.domain.service.ClassService
import com.thoughtworks.archguard.clazz.domain.JClass
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/{projectId}/classes")
class ClassController {

    @Autowired
    private lateinit var service: ClassService

    @GetMapping("/{name}/dependencies")
    fun getDependencies(@PathVariable("projectId") projectId: Long,
                        @PathVariable("name") name: String,
                        @RequestParam(value = "module", required = false, defaultValue = "") module: String,
                        @RequestParam("deep", required = false, defaultValue = "3") deep: Int): JClass {
        return service.getDependencies(module, name, deep)
    }

    @GetMapping("/{name}/invokes")
    fun getInvokes(@PathVariable("projectId") projectId: Long,
                   @PathVariable("name") name: String,
                   @RequestParam(value = "module", required = false, defaultValue = "") module: String,
                   @RequestParam(value = "deep", required = false, defaultValue = "3") deep: Int,
                   @RequestParam(value = "callerDeep", required = false) callerDeep: Int?,
                   @RequestParam(value = "calleeDeep", required = false) calleeDeep: Int?,
                   @RequestParam(value = "needIncludeImpl", required = false, defaultValue = "true") needIncludeImpl: Boolean?): JClass {
        return service.findInvokes(projectId, module, name, callerDeep ?: deep, calleeDeep ?: deep, needIncludeImpl ?: true)
    }

    @GetMapping("/{name}/methods_callees")
    fun getMethodsCallees(@PathVariable("projectId") projectId: Long,
                          @PathVariable("name") name: String,
                          @RequestParam(value = "module", required = false, defaultValue = "") module: String,
                          @RequestParam(value = "deep", required = false, defaultValue = "3") deep: Int,
                          @RequestParam(value = "needParents", required = false, defaultValue = "true") needParents: Boolean,
                          @RequestParam(value = "needIncludeImpl", required = false, defaultValue = "true") needIncludeImpl: Boolean): JClass {
        return service.findMethodsCallees(module, name, deep, needIncludeImpl, needParents)
    }
}
