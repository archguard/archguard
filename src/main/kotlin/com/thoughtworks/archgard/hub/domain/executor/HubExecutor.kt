package com.thoughtworks.archgard.hub.domain.executor

import com.thoughtworks.archgard.hub.domain.helper.ScannerManager
import com.thoughtworks.archgard.hub.domain.model.HubLifecycle
import com.thoughtworks.archgard.hub.domain.repository.HubRepository
import com.thoughtworks.archgard.hub.util.FileUtil
import com.thoughtworks.archgard.scanner.domain.ScanContext
import org.eclipse.jgit.api.Git
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader


@Component
class HubExecutor : HubLifecycle {

    @Autowired
    lateinit var hubRepository: HubRepository

    @Autowired
    lateinit var fileUtil: FileUtil

    override fun projectInfo(context: ScanContext) {
        context.repo = hubRepository.getProjectInfo().gitRepo
    }

    override fun getSource(context: ScanContext) {
        context.sourcePath = Git.cloneRepository()
                .setDirectory(File(context.workspace))
                .setURI(context.repo)
                .call()
                .repository
                .directory
                .absolutePath
    }

    override fun buildSource(context: ScanContext) {
        val pb = if (File(context.workspace).listFiles().orEmpty().any { it.name == "pom.xml" }) {
            ProcessBuilder("mvn", "clean", "package", "-DskipTests")
        } else {
            ProcessBuilder("./gradlew", "clean", "build", "-x", "test")
        }
        pb.redirectErrorStream(true)
        pb.directory(File(context.workspace))
        val p = pb.start()
        val inputStream: InputStream = p.inputStream
        //转成字符输入流
        val inputStreamReader = InputStreamReader(inputStream, "gbk")
        var len = -1
        val c = CharArray(1024)
        val outputString = StringBuffer()
        //读取进程输入流中的内容
        while (inputStreamReader.read(c).also { len = it } != -1) {
            val s = String(c, 0, len)
            outputString.append(s)
            print(s)
        }
        inputStream.close()
    }

    override fun getScanner(context: ScanContext, manager: ScannerManager) = manager.load()

    override fun execute(context: ScanContext, manager: ScannerManager) = manager.execute(context)

    override fun clean(context: ScanContext) {
        fileUtil.cleanAll(context.workspace)
    }

}