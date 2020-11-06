package com.thoughtworks.archguard.system_info.controller

import com.thoughtworks.archguard.common.CreateFileUtil
import com.thoughtworks.archguard.system_info.domain.SystemInfo
import com.thoughtworks.archguard.system_info.domain.SystemInfoService
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDate

@RestController
@RequestMapping("/system-info")
class SystemInfoController(
        @Value("\${module.zipFilePath}") val zipFilePath: String,
        val systemInfoService: SystemInfoService,
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
    fun addSystemInfo(@RequestBody systemInfoDTO: SystemInfoDTO): Long {
        val systemInfo: SystemInfo = systemInfoMapper.fromDTO(systemInfoDTO)
        return systemInfoService.addSystemInfo(systemInfo)
    }

    @DeleteMapping("/{id}")
    fun deleteSystemInfo(@PathVariable("id") id: Long) = systemInfoService.deleteSystemInfo(id)

    @PostMapping("/upload")
    fun uploadZip(@RequestParam("file") file: MultipartFile): String {
        if (file.isEmpty) {
            return "上传失败，请选择文件"
        }



        val dir = createTempDir("temp",null, CreateFileUtil.createDir("/tmp/zip/files"))
//        val createDir = CreateFileUtil.createDir(zipFilePath)


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
