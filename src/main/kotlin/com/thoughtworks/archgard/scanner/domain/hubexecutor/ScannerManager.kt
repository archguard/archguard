package com.thoughtworks.archgard.scanner.domain.hubexecutor

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.config.model.ToolConfigure
import com.thoughtworks.archgard.scanner.domain.config.model.getConfigNames
import com.thoughtworks.archgard.scanner.domain.config.repository.ConfigureRepository
import com.thoughtworks.archgard.scanner.domain.scanner.Scanner
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.Callable
import java.util.concurrent.Executors


@Component
class ScannerManager(@Autowired private val scanners: List<Scanner>) {

    @Autowired
    private lateinit var configureRepository: ConfigureRepository

    private val log = LoggerFactory.getLogger(ScannerManager::class.java)

    fun execute(context: ScanContext) {
        val WORKER_THREAD_POOL = Executors.newFixedThreadPool(4)


        val callables: List<Callable<Unit>> = scanners.map { s ->
            Callable {
                try {
                    s.scan(context)
                } catch (e: Exception) {
                    log.error("failed to scan {}", s.javaClass.simpleName, e)
                }
            }
        }

        WORKER_THREAD_POOL.invokeAll(callables)
    }

    fun register() {
        val toRegister = scanners.map { it.toolList }.flatten().map { getConfigNames(it) }.flatten()
        val registered = configureRepository.getConfigures()
                .groupBy { it.type }.mapValues {
                    val temp = HashMap<String, String>()
                    it.value.forEach { i ->
                        temp[i.key] = i.value
                    }
                    temp
                }.map { ToolConfigure(it.key, it.value) }.map { getConfigNames(it) }.flatten()

        configureRepository.register(toRegister.filter { !registered.contains(it) })
        configureRepository.cleanRegistered(registered.filter { !toRegister.contains(it) })
    }
}