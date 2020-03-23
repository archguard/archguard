package com.thoughtworks.archgard.scanner.domain.hubexecutor

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.infrastructure.FileOperator
import org.eclipse.jgit.api.Git
import java.io.InputStream
import java.io.InputStreamReader

class HubExecutor(private val context: ScanContext, private val manager: ScannerManager) : HubLifecycle {

    override fun execute() {
        getSource()
        buildSource()
        manager.execute(context)
    }

    override fun clean() {
        FileOperator.deleteDirectory(context.workspace)
    }

    private fun getSource() {
        Git.cloneRepository()
                .setDirectory(context.workspace)
                .setURI(context.repo)
                .call()
    }

    private fun buildSource() {
        val pb = if (context.workspace.listFiles().orEmpty().any { it.name == "pom.xml" }) {
            ProcessBuilder("./mvnw", "clean", "package", "-DskipTests")
        } else {
            ProcessBuilder("./gradlew", "clean", "build", "-x", "test")
        }
        pb.redirectErrorStream(true)
        pb.directory(context.workspace)
        val p = pb.start()
        val inputStream: InputStream = p.inputStream
        //转成字符输入流
        val inputStreamReader = InputStreamReader(inputStream, "gbk")
        var len: Int
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


}