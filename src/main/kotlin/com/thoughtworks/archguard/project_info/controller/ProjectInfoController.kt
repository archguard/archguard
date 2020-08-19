package com.thoughtworks.archguard.project_info.controller

import com.thoughtworks.archguard.project_info.domain.ProjectInfoDTO
import com.thoughtworks.archguard.project_info.domain.ProjectInfoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@RestController
@RequestMapping("/project-info")
class ProjectInfoController {

    @Autowired
    lateinit var projectInfoService: ProjectInfoService

    @GetMapping("/{id}")
    fun getProjectInfo(@PathVariable("id") id: Long) = projectInfoService.getProjectInfo(id)

    @GetMapping
    fun getAllProjectInfo() = projectInfoService.getAllProjectInfo()

    @PutMapping
    fun updateProjectInfo(@RequestBody projectInfoDTO: ProjectInfoDTO) = projectInfoService.updateProjectInfo(projectInfoDTO)

    @PostMapping
    fun addProjectInfo(@RequestBody projectInfoDTO: ProjectInfoDTO) = projectInfoService.addProjectInfo(projectInfoDTO)

    @DeleteMapping("/{id}")
    fun deleteProjectInfo(@PathVariable("id") id: Long) = projectInfoService.deleteProjectInfo(id)

    @PostMapping("/upload")
    fun uploadZip(@RequestParam("file") file: MultipartFile): String {
        if (file.isEmpty) {
            return "上传失败，请选择文件"
        }

        val dir = createTempDir()
        val fileName = file.originalFilename ?: "Project-Info"
        val filePath = this.prepareZipFile(dir.absolutePath, fileName)
        file.transferTo(filePath.toFile())
        return filePath.toString()
    }

    private fun prepareZipFile(dir: String, fileName: String): Path {
        val filePath = Paths.get(dir, fileName)
        if (Files.exists(filePath)) {
            Files.delete(filePath)
        }

        return Files.createFile(filePath)
    }
}
