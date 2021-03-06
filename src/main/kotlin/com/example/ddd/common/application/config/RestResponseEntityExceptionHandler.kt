package com.example.ddd.common.application.config

import com.example.ddd.common.application.errors.ErrorResponse
import com.example.ddd.order.domain.errors.client.ClientNotFoundException
import com.example.ddd.order.domain.errors.order.OrderNotFoundException
import com.example.ddd.order.domain.errors.product.InvalidProductPriceException
import com.example.ddd.order.domain.errors.product.InvalidProductStatusException
import com.example.ddd.order.domain.errors.product.InvalidProductStockException
import com.example.ddd.order.domain.errors.product.ProductNotFoundException
import com.example.ddd.order.domain.errors.product.ProductsNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

  @ExceptionHandler(
    value = [
      OrderNotFoundException::class,
      ProductNotFoundException::class,
      ProductsNotFoundException::class,
      ClientNotFoundException::class
    ]
  )
  fun handleResourceNotFoundException(e: Exception): ResponseEntity<ErrorResponse> {
    return ResponseEntity(ErrorResponse.RESOURCE_NOT_FOUND, HttpStatus.NOT_FOUND)
  }

  @ExceptionHandler(
    value = [
      InvalidProductPriceException::class,
      InvalidProductStatusException::class,
      InvalidProductStockException::class
    ]
  )
  fun handleInvalidProductStatusException(e: Exception): ResponseEntity<ErrorResponse> {
    return ResponseEntity(ErrorResponse.BAD_REQUEST, HttpStatus.BAD_REQUEST)
  }
}