package com.thoughtworks.archguard.report.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/systems/{systemId}/cohesion")
class CohesionController {

    @GetMapping("/shotgun-surgery")
    fun getClassesDataClumpsWithTotalCount(@PathVariable("systemId") systemId: Long,
                                           @RequestParam(value = "numberPerPage") limit: Long,
                                           @RequestParam(value = "currentPageNumber") currentPageNumber: Long): ResponseEntity<ShotgunSurgeryListDto> {
        return ResponseEntity.ok(ShotgunSurgeryListDto(listOf(
                ShotgunSurgery("rew34rnlfds", "feat: init",
                        listOf(Clazz("spring-bean", "org.spring", "SpringA"),
                                Clazz("spring-bean", "org.spring", "SpringF"),
                                Clazz("spring-bean", "org.spring", "SpringM"),
                                Clazz("spring-bean", "org.spring", "SpringS"),
                                Clazz("spring-bean", "org.spring", "SpringJ"),
                                Clazz("spring-bean", "org.spring", "SpringH"),
                                Clazz("spring-bean", "org.spring", "SpringL"),
                                Clazz("spring-bean", "org.spring", "SpringU"),
                                Clazz("spring-bean", "org.spring", "SpringQ"))),
                ShotgunSurgery("rew34rnlfds", "feat: add",
                        listOf(Clazz("spring-bean", "org.spring", "SpringM"),
                                Clazz("spring-bean", "org.spring", "SpringL"),
                                Clazz("spring-bean", "org.spring", "SpringE"),
                                Clazz("spring-bean", "org.spring", "SpringR"),
                                Clazz("spring-bean", "org.spring", "SpringO"),
                                Clazz("spring-bean", "org.spring", "SpringL"),
                                Clazz("spring-bean", "org.spring", "SpringP"),
                                Clazz("spring-bean", "org.spring", "SpringA"),
                                Clazz("spring-bean", "org.spring", "SpringB"))),
                ShotgunSurgery("2enldsjlk2d", "feat: delete",
                        listOf(Clazz("spring-bean", "org.spring", "SpringC"),
                                Clazz("spring-bean", "org.spring", "SpringO"),
                                Clazz("spring-bean", "org.spring", "SpringX"),
                                Clazz("spring-bean", "org.spring", "SpringM"),
                                Clazz("spring-bean", "org.spring", "SpringN"),
                                Clazz("spring-bean", "org.spring", "SpringV"),
                                Clazz("spring-bean", "org.spring", "SpringG"),
                                Clazz("spring-bean", "org.spring", "SpringS"),
                                Clazz("spring-bean", "org.spring", "Spring2"),
                                Clazz("spring-bean", "org.spring", "SpringD"))),
                ShotgunSurgery("fewre32fdsfd", "feat: update",
                        listOf(Clazz("spring-bean", "org.spring", "SpringD"),
                                Clazz("spring-bean", "org.spring", "SpringX"),
                                Clazz("spring-bean", "org.spring", "SpringA"),
                                Clazz("spring-bean", "org.spring", "SpringC"),
                                Clazz("spring-bean", "org.spring", "SpringM"),
                                Clazz("spring-bean", "org.spring", "SpringN"),
                                Clazz("spring-bean", "org.spring", "SpringV"),
                                Clazz("spring-bean", "org.spring", "SpringG"),
                                Clazz("spring-bean", "org.spring", "SpringS"),
                                Clazz("spring-bean", "org.spring", "SpringC"))),
                ShotgunSurgery("fds2349dsn32", "feat: update112",
                        listOf(Clazz("spring-bean", "org.spring", "SpringE"),
                                Clazz("spring-bean", "org.spring", "SpringA"),
                                Clazz("spring-bean", "org.spring", "SpringC"),
                                Clazz("spring-bean", "org.spring", "SpringM"),
                                Clazz("spring-bean", "org.spring", "SpringN"),
                                Clazz("spring-bean", "org.spring", "SpringV"),
                                Clazz("spring-bean", "org.spring", "SpringG"),
                                Clazz("spring-bean", "org.spring", "SpringS"),
                                Clazz("spring-bean", "org.spring", "SpringC")))
        ), 13, 1))
    }
}

data class ShotgunSurgeryListDto(val data: List<ShotgunSurgery>, val count: Long, val currentPageNumber: Long)
data class ShotgunSurgery(val commitId: String, val commitMessage: String, val clazzes: List<Clazz>)
data class Clazz(val moduleName: String? = null, val packageName: String, val typeName: String)