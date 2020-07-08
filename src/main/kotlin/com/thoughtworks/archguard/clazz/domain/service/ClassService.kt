package com.thoughtworks.archguard.clazz.domain.service

import com.thoughtworks.archguard.clazz.domain.service.ClassDependenceesService
import com.thoughtworks.archguard.clazz.domain.service.ClassDependencerService
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClass
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ClassService {
    @Autowired
    private lateinit var classDependenceesService: ClassDependenceesService
    @Autowired
    private lateinit var classDependencerService: ClassDependencerService
    fun findDependencies(name: String, deep: Int): Dependency<List<JClass>> {
        val callees = classDependenceesService.findDependencees(name, deep)
        val callers = classDependencerService.findDependencers(name, deep)
        return Dependency(callers, callees)
    }

}
