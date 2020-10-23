package com.thoughtworks.archguard.system_info.controller

import com.thoughtworks.archguard.system_info.domain.SystemInfo
import com.thoughtworks.archguard.system_info.domain.SystemInfoService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@RestController
@RequestMapping("/system-info")
class SystemInfoController(val systemInfoService: SystemInfoService,
                           val systemInfoMapper: SystemInfoMapper) {


    @GetMapping("/{id}")
    fun getSystemInfo(@PathVariable("id") id: Long): SystemInfoDTO {
        val systemInfo = systemInfoService.getSystemInfo(id)
        return systemInfoMapper.toDTO(systemInfo)
    }

    @GetMapping
    fun getAllSystemInfo(): List<SystemInfoDTO> {
        return systemInfoService.getAllSystemInfo().map(systemInfoMapper::toDTO)
    }

    @PutMapping
    fun updateSystemInfo(@RequestBody systemInfoDTO: SystemInfoDTO) {
        val systemInfo: SystemInfo = systemInfoMapper.fromDTO(systemInfoDTO)
        systemInfoService.updateSystemInfo(systemInfo)
    }

    @PostMapping
    fun addSystemInfo(@RequestBody systemInfoDTO: SystemInfoDTO) {
        val systemInfo: SystemInfo = systemInfoMapper.fromDTO(systemInfoDTO)
        systemInfoService.addSystemInfo(systemInfo)
    }

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
