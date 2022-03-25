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

@RestController
@RequestMapping("/api/system-info")
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


//         createTempDir("temp", null, CreateFileUtil.createDir("/tmp/zip/files"))
//        val createDir = CreateFileUtil.createDir(zipFilePath)


        try {
            val dir = CreateFileUtil.createDir(zipFilePath + "/" + System.nanoTime() + ".temp")
            val fileName = file.originalFilename ?: "System-Info"
            val filePath = dir?.absolutePath?.let { this.prepareZipFile(it, fileName) }
            if (filePath != null) {
                file.transferTo(filePath.toFile())
            }
            return filePath.toString()
        } catch (e : Exception) {
            println("常见目录失败")
            return "创建目录失败"
        }

    }


    private fun prepareZipFile(dir: String, fileName: String): Path {
        val filePath = Paths.get(dir, fileName)
        if (Files.exists(filePath)) {
            Files.delete(filePath)
        }

        return Files.createFile(filePath)
    }
}
