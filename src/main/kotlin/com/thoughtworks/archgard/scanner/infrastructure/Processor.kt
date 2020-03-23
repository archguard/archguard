package com.thoughtworks.archgard.scanner.infrastructure

import java.io.File
import java.io.InputStream
import java.io.InputStreamReader

object Processor {

    fun executeWithLogs(pb: ProcessBuilder, workspace: File) {
        pb.redirectErrorStream(true)
        pb.directory(workspace)
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