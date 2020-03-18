package com.thoughtworks.archgard.scanner.infrastructure

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.nio.channels.Channels

object FileDownloader {

    fun download(url: URL, file: File) {
        try {
            val readableByteChannel = Channels.newChannel(url.openStream())
            val fileOutputStream = FileOutputStream(file)
            fileOutputStream.channel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE)
        } catch (e: IOException) {
            // TODO: 2020/3/18 add log
        }
    }
}