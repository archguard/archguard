package com.thoughtworks.archgard.scanner2.common

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.CancellationException
import java.util.concurrent.ExecutionException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


@Component
class Scanner2ThreadPool {

    private val fixedThread: ExecutorService = Scanner2ExtendedExecutor(5)

    fun submit(task: Runnable) {
        fixedThread.submit(task)
    }

}

class Scanner2ExtendedExecutor(nThread: Int) : ThreadPoolExecutor(nThread, nThread,
        0L, TimeUnit.MILLISECONDS,
        LinkedBlockingQueue()) {
    private val log = LoggerFactory.getLogger(Scanner2ExtendedExecutor::class.java)

    override fun afterExecute(r: Runnable, t: Throwable?) {
        var t = t
        super.afterExecute(r, t)
        if (t == null && r is Future<*>) {
            try {
                (r as Future<*>).get()
            } catch (ce: CancellationException) {
                t = ce
            } catch (ee: ExecutionException) {
                t = ee.cause
            } catch (ie: InterruptedException) {
                Thread.currentThread().interrupt() // ignore/reset
            }
        }
        t?.let {
            throw RuntimeException("Scanner2ExtendedExecutor run with: $it")
        }
    }
}