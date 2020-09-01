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

        val specificClassStatisticItem = classStatistic.find { it.moduleName == "dubbo-service" && it.typeName == "User" }
        assertEquals("dubbo-service", specificClassStatisticItem?.moduleName)
        assertEquals("net.aimeizi.dubbo.service.entity", specificClassStatisticItem?.packageName)
        assertEquals("User", specificClassStatisticItem?.typeName)
        assertEquals(45, specificClassStatisticItem?.lines)

        val methodStatistic = jdbi.withHandle<List<MethodStatistic>, RuntimeException> { handle: Handle ->
            handle.createQuery("select * from MethodStatistic")
                    .mapTo(MethodStatistic::class.java).list()
        }
        val specificMethodStatistic = methodStatistic.find { it.moduleName == "dubbo-service" && it.typeName == "User" && it.methodName == "getUserId"}

        assertEquals(21, methodStatistic.size)
        assertEquals("dubbo-service", specificMethodStatistic?.moduleName)
        assertEquals("net.aimeizi.dubbo.service.entity", specificMethodStatistic?.packageName)
        assertEquals("User", specificMethodStatistic?.typeName)
        assertEquals("User", specificMethodStatistic?.typeName)
        assertEquals("getUserId", specificMethodStatistic?.methodName)
        assertEquals(3, specificMethodStatistic?.lines)
    }

    @Test
    fun should_generate_statistics_given_single_module_example() {
        statisticScanner.scan(ScanContext("", BuildTool.GRADLE, File(javaClass.classLoader.getResource("TestStatistic/single-module-example").toURI()), ArrayList()))

        val classStatistic = jdbi.withHandle<List<ClassStatistic>, RuntimeException> { handle: Handle ->
            handle.createQuery("select * from ClassStatistic")
                    .mapTo(ClassStatistic::class.java).list()
        }
        assertEquals(23, classStatistic.size)

        val specificClassStatisticItem = classStatistic.find { it.typeName == "SmartParkingBoyTest" }
        assertEquals(null, specificClassStatisticItem?.moduleName)
        assertEquals("com.qicaisheng.parkinglot", specificClassStatisticItem?.packageName)
        assertEquals("SmartParkingBoyTest", specificClassStatisticItem?.typeName)
        assertEquals(50, specificClassStatisticItem?.lines)

        val methodStatistic = jdbi.withHandle<List<MethodStatistic>, RuntimeException> { handle: Handle ->
            handle.createQuery("select * from MethodStatistic")
                    .mapTo(MethodStatistic::class.java).list()
        }

        assertEquals(97, methodStatistic.size)
        
        val specificMethodStatistic = methodStatistic.find { it.methodName == "should_be_picked_up_from_managed_parking_lot_when_smart_park_boy_park_card"}
        assertEquals(null, specificMethodStatistic?.moduleName)
        assertEquals("com.qicaisheng.parkinglot", specificMethodStatistic?.packageName)
        assertEquals("SmartParkingBoyTest", specificMethodStatistic?.typeName)
        assertEquals("should_be_picked_up_from_managed_parking_lot_when_smart_park_boy_park_card", specificMethodStatistic?.methodName)
        assertEquals(8, specificMethodStatistic?.lines)
    }
}