package com.thoughtworks.archguard.scanner.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.createTempDirectory

@RestController
@RequestMapping("/api/scanner/file")
class FileController {

    @PostMapping("/upload")
    fun uploadZip(@RequestParam("file") file: MultipartFile): String {
        if (file.isEmpty) {
            return "上传失败，请选择文件"
        }

        val dir = createTempDirectory().toFile()
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
