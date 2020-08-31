package com.thoughtworks.archguard.system_info.controller

import com.thoughtworks.archguard.system_info.domain.SystemInfoDTO
import com.thoughtworks.archguard.system_info.domain.SystemInfoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@RestController
@RequestMapping("/system-info")
class SystemInfoController {

    @Autowired
    lateinit var systemInfoService: SystemInfoService

    @GetMapping("/{id}")
    fun getSystemInfo(@PathVariable("id") id: Long) = systemInfoService.getSystemInfo(id)

    @GetMapping
    fun getAllSystemInfo() = systemInfoService.getAllSystemInfo()

    @PutMapping
    fun updateSystemInfo(@RequestBody systemInfoDTO: SystemInfoDTO) = systemInfoService.updateSystemInfo(systemInfoDTO)

    @PostMapping
    fun addSystemInfo(@RequestBody systemInfoDTO: SystemInfoDTO) = systemInfoService.addSystemInfo(systemInfoDTO)

    @DeleteMapping("/{id}")
    fun deleteSystemInfo(@PathVariable("id") id: Long) = systemInfoService.deleteSystemInfo(id)

    @PostMapping("/upload")
    fun uploadZip(@RequestParam("file") file: MultipartFile): String {
        if (file.isEmpty) {
            return "上传失败，请选择文件"
        }

        val dir = createTempDir()
        val fileName = file.originalFilename ?: "System-Info"
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
