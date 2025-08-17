package org.deblock.exercise.infrastructure.adapter.`in`.rest

import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleValidationException(ex: ConstraintViolationException): ResponseEntity<String> {
        return ResponseEntity(ex.message, BAD_REQUEST)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleValidationException(ex: IllegalArgumentException): ResponseEntity<String> {
        return ResponseEntity(ex.message, BAD_REQUEST)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleValidationException(ex: RuntimeException): ResponseEntity<String> {
        // do not reveal internal errors to the client, report to monitoring instead
        logger.error("Request processing error", ex)
        return ResponseEntity("Something went wrong", INTERNAL_SERVER_ERROR)
    }

    // add others as needed

    companion object {
        private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }
}