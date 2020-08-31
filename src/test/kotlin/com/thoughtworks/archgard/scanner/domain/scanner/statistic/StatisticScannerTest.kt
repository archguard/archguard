package com.thoughtworks.archgard.scanner.domain.scanner.statistic

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.system.BuildTool
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.io.File

@SpringBootTest
@ActiveProfiles("test")
internal class StatisticScannerTest(@Autowired val statisticScanner: StatisticScanner, @Autowired val jdbi: Jdbi) {
    @Test
    fun should_generate_statistics_given_multiple_modules_examples() {
        statisticScanner.scan(ScanContext("", BuildTool.MAVEN, File(javaClass.classLoader.getResource("TestStatistic/multiple-modules-example").toURI()), ArrayList()))

        val classStatistic = jdbi.withHandle<List<ClassStatistic>, RuntimeException> { handle: Handle ->
            handle.createQuery("select * from ClassStatistic")
                    .mapTo(ClassStatistic::class.java).list()
        }
        assertEquals(8, classStatistic.size)
        assertEquals("net.aimeizi.dubbo.service.entity", classStatistic[0].packageName)
        assertEquals("User", classStatistic[0].typeName)
        assertEquals(45, classStatistic[0].lines)

        val methodStatistic = jdbi.withHandle<List<MethodStatistic>, RuntimeException> { handle: Handle ->
            handle.createQuery("select * from MethodStatistic")
                    .mapTo(MethodStatistic::class.java).list()
        }
        assertEquals(21, methodStatistic.size)
        assertEquals("net.aimeizi.dubbo.service.entity", methodStatistic[0].packageName)
        assertEquals("User", methodStatistic[0].typeName)
        assertEquals("User", methodStatistic[0].typeName)
        assertEquals("getUserId", methodStatistic[0].methodName)
        assertEquals(3, methodStatistic[0].lines)
    }

    @Test
    fun should_generate_statistics_given_single_module_example() {
        statisticScanner.scan(ScanContext("", BuildTool.MAVEN, File(javaClass.classLoader.getResource("TestStatistic/single-module-example").toURI()), ArrayList()))

        val classStatistic = jdbi.withHandle<List<ClassStatistic>, RuntimeException> { handle: Handle ->
            handle.createQuery("select * from ClassStatistic")
                    .mapTo(ClassStatistic::class.java).list()
        }
        assertEquals(8, classStatistic.size)
        assertEquals("net.aimeizi.dubbo.service.entity", classStatistic[0].packageName)
        assertEquals("User", classStatistic[0].typeName)
        assertEquals(45, classStatistic[0].lines)

        val methodStatistic = jdbi.withHandle<List<MethodStatistic>, RuntimeException> { handle: Handle ->
            handle.createQuery("select * from MethodStatistic")
                    .mapTo(MethodStatistic::class.java).list()
        }
        assertEquals(21, methodStatistic.size)
        assertEquals("net.aimeizi.dubbo.service.entity", methodStatistic[0].packageName)
        assertEquals("User", methodStatistic[0].typeName)
        assertEquals("User", methodStatistic[0].typeName)
        assertEquals("getUserId", methodStatistic[0].methodName)
        assertEquals(3, methodStatistic[0].lines)
    }
}