package com.thoughtworks.archguard.common.exception

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

    @ExceptionHandler(value = [Exception::class])
    @ResponseBody
    fun genericExceptionHandler(e: Exception): ResponseEntity<*>? {
        logger.warn("Exception with error  {}", e)
        return ResponseEntity<Any?>(e.message, HttpStatus.BAD_REQUEST)
    }
}