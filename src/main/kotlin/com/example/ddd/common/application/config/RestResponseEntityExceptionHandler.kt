package com.example.ddd.common.application.config

import com.example.ddd.common.application.errors.ErrorResponse
import com.example.ddd.product.domain.errors.InvalidProductStatusException
import com.example.ddd.product.domain.errors.ProductNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

  @ExceptionHandler(ProductNotFoundException::class)
  fun handleResourceNotFoundException(e: Exception): ResponseEntity<ErrorResponse> {
    return ResponseEntity(ErrorResponse.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND)
  }

  @ExceptionHandler(InvalidProductStatusException::class)
  fun handleInvalidProductStatusException(e: Exception): ResponseEntity<ErrorResponse> {
    return ResponseEntity(ErrorResponse.BAD_REQUEST, HttpStatus.BAD_REQUEST)
  }
}