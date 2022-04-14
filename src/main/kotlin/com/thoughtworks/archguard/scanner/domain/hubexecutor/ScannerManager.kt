package com.thoughtworks.archguard.scanner.domain.hubexecutor

import com.thoughtworks.archguard.scanner.domain.config.repository.ScannerConfigureRepository
import com.thoughtworks.archguard.scanner.domain.scanner.Scanner
import com.thoughtworks.archguard.scanner.domain.scanner.javaext.bs.ScanContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.Callable
import java.util.concurrent.Executors

@Component
class ScannerManager(@Autowired private val scanners: List<Scanner>) {

    @Autowired
    private lateinit var configureRepository: ScannerConfigureRepository

    private val log = LoggerFactory.getLogger(ScannerManager::class.java)

    fun execute(context: ScanContext) {
        val WORKER_THREAD_POOL = Executors.newFixedThreadPool(4)
        val callables: List<Callable<Unit>> = scanners.map { s ->
            Callable {
                try {
                    if (s.canScan(context)) {
                        s.scan(context)
                    }
                } catch (e: Exception) {
                    log.error("failed to scan {}", s.javaClass.simpleName, e)
                }
            }
        }

        WORKER_THREAD_POOL.invokeAll(callables)
    }

    fun register() {
        val toRegister = scanners.map { it.toolList }.flatten().map { it.getConfigNames() }.flatten()
        val registered = configureRepository.getToolConfigures().map { it.getConfigNames() }.flatten()

        configureRepository.register(toRegister.filter { !registered.contains(it) })
        configureRepository.cleanRegistered(registered.filter { !toRegister.contains(it) })
    }
}
