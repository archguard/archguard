package com.thoughtworks.archgard.scanner.domain.hubexecutor

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.config.repository.ConfigureRepository
import com.thoughtworks.archgard.scanner.domain.scanner.Scanner
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class ScannerManager(@Autowired private val scanners: List<Scanner>) {

    @Autowired
    private lateinit var configureRepository: ConfigureRepository

    private val log = LoggerFactory.getLogger(ScannerManager::class.java)

    fun execute(context: ScanContext) {
//        val WORKER_THREAD_POOL = Executors.newFixedThreadPool(4)

        val registered = configureRepository.getRegistered().filter { it.value == "true" }.map { it.type }

//        val callables: List<Callable<Unit>> = scanners.map { s ->
//            Callable {
//                try {
//                    s.scan(context)
//                } catch (e: Exception) {
//                    log.error("failed to scan {}", s.javaClass.simpleName, e)
//                }
//            }
//        }
//
//        WORKER_THREAD_POOL.invokeAll(callables)

        scanners.forEach {
            try {
                log.info("start to scan {}", it.javaClass.simpleName)
                it.scan(context)
            } catch (e: Exception) {
                log.error("failed to scan {}", it.javaClass.simpleName, e)
            }
        }
    }

    fun register() {
        val types = configureRepository.getRegistered().map { it.type }
        val names = scanners.map { it.name }
        configureRepository.register(names.filter { !types.contains(it) })
        configureRepository.cleanRegistered(types.filter { !names.contains(it) })
    }
}