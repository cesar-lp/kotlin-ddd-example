package com.example.ddd.order.application.controllers

import com.example.ddd.common.application.errors.ErrorResponse
import com.example.ddd.order.application.controllers.requests.AddProductStockRequest
import com.example.ddd.order.application.controllers.requests.ChangeProductStatusRequest
import com.example.ddd.order.application.controllers.requests.CreateProductRequest
import com.example.ddd.order.application.controllers.requests.UpdateProductRequest
import com.example.ddd.order.application.controllers.responses.ProductResponse
import com.example.ddd.order.domain.models.entities.ProductStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import java.math.BigDecimal

class ProductControllerTest : BaseControllerTest() {

  private val BEER_PRODUCT_ID = "prd-8f6f04dc-dc73-11ec-9d64-0242ac120002"
  private val STEAK_PRODUCT_ID = "prd-95956b62-dc73-11ec-9d64-0242ac120002"

  @Nested
  @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
  @DisplayName("GET - /products/{id}")
  inner class GetProduct {

    @Test
    fun `should get a product`() {
      val response = restTemplate.getForEntity("/products/${BEER_PRODUCT_ID}", ProductResponse::class.java)
      val product = response.body!!

      val expectedProduct = product.copy(
        id = "prd-8f6f04dc-dc73-11ec-9d64-0242ac120002",
        name = "Beer",
        description = "Enjoy your day with a nice cold beer",
        status = "enabled",
        stock = 10,
        price = "2.50"
      )

      assertEquals(HttpStatus.OK, response.statusCode)
      assertEquals(expectedProduct, product)
    }

    @Test
    fun `should throw an exception for a non existing product`() {
      val response = restTemplate.getForEntity("/products/prd-0", ErrorResponse::class.java)

      val expectedError = ErrorResponse(
        "RESOURCE_NOT_FOUND",
        "Not found",
        "The requested resource could not be found"
      )

      assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
      assertEquals(expectedError, response.body)
    }
  }

  @Nested
  @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
  @DisplayName("POST - /products")
  inner class CreateProduct {

    @Test
    fun `should create a product`() {
      val request = CreateProductRequest(name = "Coke", description = "Coke can", price = BigDecimal("2.50"))

      val response = restTemplate.postForEntity("/products", request, ProductResponse::class.java)
      val product = response.body!!

      val expectedProduct = product.copy(
        name = "Coke",
        description = "Coke can",
        status = "enabled",
        price = "2.50"
      )

      assertEquals(HttpStatus.CREATED, response.statusCode)
      assertEquals(expectedProduct, product)
    }
  }

  @Nested
  @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
  @DisplayName("PUT - /products/{id}")
  inner class UpdateProduct {

    @Test
    fun `should update a product`() {
      val request = UpdateProductRequest("New product name", "New product description", BigDecimal("2.50"))

      val response = restTemplate.exchange(
        "/products/${STEAK_PRODUCT_ID}", HttpMethod.PUT, HttpEntity(request), ProductResponse::class.java
      )

      val product = response.body!!

      val expectedProduct = product.copy(
        id = "prd-95956b62-dc73-11ec-9d64-0242ac120002",
        name = "New product name",
        description = "New product description",
        status = "enabled",
        stock = 5,
        price = "2.50"
      )

      assertEquals(HttpStatus.OK, response.statusCode)
      assertEquals(expectedProduct, product)
    }

    @Test
    fun `should throw an exception when the product does not exist`() {
      val request = UpdateProductRequest("New product name", "New product description", price = BigDecimal("2.50"))

      val response = restTemplate.exchange(
        "/products/prd-0", HttpMethod.PUT, HttpEntity(request), ErrorResponse::class.java
      )

      val expectedError = ErrorResponse(
        "RESOURCE_NOT_FOUND",
        "Not found",
        "The requested resource could not be found"
      )

      assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
      assertEquals(expectedError, response.body)
    }

    @Test
    fun `should throw an exception if the new price is invalid`() {
      val request = UpdateProductRequest("New product name", "New product description", price = BigDecimal("0"))

      val response = restTemplate.exchange(
        "/products/${BEER_PRODUCT_ID}", HttpMethod.PUT, HttpEntity(request), ErrorResponse::class.java
      )

      val expectedError = ErrorResponse(
        "BAD_REQUEST",
        "Bad request",
        "Bad request"
      )

      assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
      assertEquals(expectedError, response.body)
    }
  }

  @Nested
  @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
  @DisplayName("PATCH - /products/{id}/status")
  inner class ChangeStatus {

    @Test
    fun `should change a product's status`() {
      val request = ChangeProductStatusRequest(ProductStatus.DISABLED.description)

      val response = restTemplate.exchange(
        "/products/${BEER_PRODUCT_ID}/status", HttpMethod.PATCH, HttpEntity(request), ProductResponse::class.java
      )
      val product = response.body!!

      val expectedProduct = product.copy(
        id = "prd-8f6f04dc-dc73-11ec-9d64-0242ac120002",
        name = "Beer",
        description = "Enjoy your day with a nice cold beer",
        status = "disabled",
        stock = 10,
        price = "2.50"
      )

      assertEquals(HttpStatus.OK, response.statusCode)
      assertEquals(expectedProduct, product)
    }

    @Test
    fun `should throw an exception if the product does not exist`() {
      val request = ChangeProductStatusRequest(ProductStatus.DISABLED.description)

      val response = restTemplate.exchange(
        "/products/prd-0/status", HttpMethod.PATCH, HttpEntity(request), ErrorResponse::class.java
      )

      val expectedError = ErrorResponse(
        "RESOURCE_NOT_FOUND",
        "Not found",
        "The requested resource could not be found"
      )

      assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
      assertEquals(expectedError, response.body)
    }

    @Test
    fun `should throw an exception if the status is invalid`() {
      val request = ChangeProductStatusRequest("invalid value")

      val response = restTemplate.exchange(
        "/products/${BEER_PRODUCT_ID}/status", HttpMethod.PATCH, HttpEntity(request), ErrorResponse::class.java
      )

      val expectedError = ErrorResponse(
        "BAD_REQUEST",
        "Bad request",
        "Bad request"
      )

      assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
      assertEquals(expectedError, response.body)
    }
  }

  @Nested
  @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
  @DisplayName("PATCH - /products/{id}/stock")
  inner class AddStock {

    @Test
    fun `should increment a product's stock`() {
      val request = AddProductStockRequest(5)

      val response = restTemplate.exchange(
        "/products/${BEER_PRODUCT_ID}/stock", HttpMethod.PATCH, HttpEntity(request), ProductResponse::class.java
      )
      val product = response.body!!

      val expectedProduct = product.copy(
        id = "prd-8f6f04dc-dc73-11ec-9d64-0242ac120002",
        name = "Beer",
        description = "Enjoy your day with a nice cold beer",
        status = "enabled",
        stock = 15,
        price = "2.50"
      )

      assertEquals(HttpStatus.OK, response.statusCode)
      assertEquals(expectedProduct, product)
    }

    @Test
    fun `should throw an exception if the product does not exist`() {
      val request = AddProductStockRequest(5)

      val response = restTemplate.exchange(
        "/products/prd-0/stock", HttpMethod.PATCH, HttpEntity(request), ErrorResponse::class.java
      )

      val expectedError = ErrorResponse(
        "RESOURCE_NOT_FOUND",
        "Not found",
        "The requested resource could not be found"
      )

      assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
      assertEquals(expectedError, response.body)
    }

    @Test
    fun `should throw an exception if the product does not have enough stock`() {
      val request = AddProductStockRequest(-50)

      val response = restTemplate.exchange(
        "/products/${BEER_PRODUCT_ID}/stock", HttpMethod.PATCH, HttpEntity(request), ErrorResponse::class.java
      )

      val expectedError = ErrorResponse(
        "BAD_REQUEST",
        "Bad request",
        "Bad request"
      )

      assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
      assertEquals(expectedError, response.body)
    }
  }
}