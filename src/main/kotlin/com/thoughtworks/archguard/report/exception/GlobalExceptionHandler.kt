package com.thoughtworks.archguard.report.exception

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

@ControllerAdvice
class GlobalExceptionHandler {

    val logger: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(value = [ThresholdNotDefinedException::class])
    @ResponseBody
    fun thresholdExceptionHandler(e: ThresholdNotDefinedException): ResponseEntity<*>? {
        logger.warn("ThresholdNotDefinedException with error  {}", e)
        return ResponseEntity<Any?>(e.message, HttpStatus.BAD_REQUEST)
    }
}
