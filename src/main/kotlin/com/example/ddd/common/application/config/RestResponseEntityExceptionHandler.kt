package com.example.ddd.common.application.config

import com.example.ddd.common.application.errors.ErrorResponse
import com.example.ddd.product.domain.errors.ProductNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(ProductNotFoundException::class)
    fun handleResourceNotFound(e: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND)
    }
}