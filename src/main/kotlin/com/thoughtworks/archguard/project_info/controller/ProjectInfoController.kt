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
    @Value("\${upload.path}")
    lateinit var UPLOAD_PATH: String;

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

    @PostMapping("/{id}/upload")
    fun uploadZip(@PathVariable("id") id: Long, @RequestParam("file") file: MultipartFile): String {
        if (file.isEmpty) {
            return "上传失败，请选择文件"
        }

        val fileName = file.originalFilename ?: "Project-Info-$id"
        val dir = this.prepareDirectory(id.toString())
        val filePath = this.prepareZipFile(dir.toString(), fileName)
        file.transferTo(filePath.toFile());
        return "上传成功"
    }

    private fun prepareDirectory(dir: String): Path {
        val path = Paths.get(UPLOAD_PATH, dir)
        path.toFile().mkdirs()
        return path
    }

    private fun prepareZipFile(dir: String, fileName: String): Path {
        val filePath = Paths.get(dir, fileName)
        if (Files.exists(filePath)) {
            Files.delete(filePath)
        }

        return Files.createFile(filePath)
    }
}
