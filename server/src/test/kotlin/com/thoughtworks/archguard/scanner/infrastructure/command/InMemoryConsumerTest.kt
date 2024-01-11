package com.thoughtworks.archguard.scanner.infrastructure.command;

import org.junit.jupiter.api.Test
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import org.slf4j.LoggerFactory

class InMemoryConsumerTest {

    @Test
    fun shouldAddLineToQueueWhenConsumeLineIsCalled() {
        // given
        val consumer = InMemoryConsumer()
        val line = "Test line"

        // when
        consumer.consumeLine(line)

        // then
        assert(consumer.asList().contains(line))
    }

    @Test
    fun shouldAddErrorMessageToQueueWhenErrorUnableToAccessJarfileIsFound() {
        // given
        val consumer = InMemoryConsumer()
        val line = "Error: Unable to access jarfile"

        // when
        consumer.consumeLine(line)

        // then
        assert(consumer.asList().contains("<ArchGuard Tips>: 'Error: Unable to access jarfile' => 下载 Scanner 可能出错，请尝试连接 VPN 下载。访问： https://archguard.org/faq 了解更多"))
    }

    @Test
    fun shouldAddErrorMessageToQueueWhenInvalidOrCorruptJarfileIsFound() {
        // given
        val consumer = InMemoryConsumer()
        val line = "Invalid or corrupt jarfile"

        // when
        consumer.consumeLine(line)

        // then
        assert(consumer.asList().contains("<ArchGuard Tips>: 'Invalid or corrupt jarfile' => jar 包不完整，请删除，并尝试连接 VPN 下载。访问： https://archguard.org/faq 了解更多"))
    }

    @Test
    fun shouldAddErrorMessageToQueueWhenFailToCloneSourceWithExitCode128IsFound() {
        // given
        val consumer = InMemoryConsumer()
        val line = "Fail to clone source with exitCode 128"

        // when
        consumer.consumeLine(line)

        // then
        assert(consumer.asList().contains("<ArchGuard Tips>: 'Fail to clone source with exitCode 128' => Git Clone 出错，尝试根据: https://archguard.org/faq#git 进行配置"))
    }

    @Test
    fun shouldAddErrorMessageToQueueWhenFailedToScanScannerIsFound() {
        // given
        val consumer = InMemoryConsumer()
        val line = "Failed to scan xxxScanner"

        // when
        consumer.consumeLine(line)

        // then
        assert(consumer.asList().contains("<ArchGuard Tips>: 'Failed to scan xxxScanner' => 检查 Scanner 是否完整? 版本是否正常? 对应版本映射关系见: https://archguard.org/release/version-mapping"))
    }
}
