package com.thoughtworks.archguard.common;

import java.io.File

object CreateFileUtil {

    fun createDir(destDirName: String): File? {
        val dir = File(destDirName)
        if (dir.exists()) {
            dir.delete()
        }

        if (!destDirName.endsWith(File.separator)) {
            destDirName.plus(File.separator)
        }
        return if (dir.mkdirs()) {
            println("创建目录" + destDirName + "成功！")
            dir
        } else {
            null
        }
    }

}
