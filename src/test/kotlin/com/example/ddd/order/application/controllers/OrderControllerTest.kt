package com.example.ddd.order.application.controllers

import com.example.ddd.common.application.errors.ErrorResponse
import com.example.ddd.order.application.controllers.requests.AddOrderProductRequest
import com.example.ddd.order.application.controllers.requests.CreateOrderRequest
import com.example.ddd.order.application.controllers.requests.OrderProductLineItemRequest
import com.example.ddd.order.application.controllers.responses.OrderResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import org.springframework.web.util.UriComponentsBuilder.fromPath

class OrderControllerTest : BaseControllerTest() {

  @Nested
  @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
  @DisplayName("POST - /orders")
  inner class CreateOrder {

    @Test
    fun `should create an order`() {
      val request = CreateOrderRequest(
        "cli-79a43474-decb-11ec-9d64-0242ac120002",
        listOf(
          OrderProductLineItemRequest("prd-8f6f04dc-dc73-11ec-9d64-0242ac120002", 2),
          OrderProductLineItemRequest("prd-95956b62-dc73-11ec-9d64-0242ac120002", 4)
        )
      )

      val response = restTemplate.postForEntity("/orders", request, OrderResponse::class.java)
      val order = response.body!!

      assertEquals(HttpStatus.CREATED, response.statusCode)

      assertNotNull(order.id)
      assertEquals(2, order.products.size)
      assertEquals("5.00", order.products[0].totalPrice)
      assertEquals(2, order.products[0].quantity)
      assertEquals("30.00", order.products[1].totalPrice)
      assertEquals(4, order.products[1].quantity)
      assertEquals("35.00", order.totalPrice)
    }

    @Test
    fun `should throw an exception if the client does not exist`() {
      val request = CreateOrderRequest(
        "cli-1",
        listOf(
          OrderProductLineItemRequest("prd-1", 2),
          OrderProductLineItemRequest("prd-99", 4)
        )
      )

      val response = restTemplate.postForEntity("/orders", request, ErrorResponse::class.java)

      val expectedError = ErrorResponse(
        "RESOURCE_NOT_FOUND",
        "Not found",
        "The requested resource could not be found"
      )

      assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
      assertEquals(expectedError, response.body)
    }

    @Test
    fun `should throw an exception if any product does not exist`() {
      val request = CreateOrderRequest(
        "cli-79a43474-decb-11ec-9d64-0242ac120002",
        listOf(
          OrderProductLineItemRequest("prd-1", 2),
          OrderProductLineItemRequest("prd-99", 4)
        )
      )

      val response = restTemplate.postForEntity("/orders", request, ErrorResponse::class.java)

      val expectedError = ErrorResponse(
        "RESOURCE_NOT_FOUND",
        "Not found",
        "The requested resource could not be found"
      )

      assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
      assertEquals(expectedError, response.body)
    }

    @Test
    fun `should throw an exception if any product does not have enough stock`() {
      val request = CreateOrderRequest(
        "cli-79a43474-decb-11ec-9d64-0242ac120002",
        listOf(
          OrderProductLineItemRequest("prd-8f6f04dc-dc73-11ec-9d64-0242ac120002", 20),
          OrderProductLineItemRequest("prd-95956b62-dc73-11ec-9d64-0242ac120002", 4)
        )
      )

      val response = restTemplate.postForEntity("/orders", request, ErrorResponse::class.java)

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
  @DisplayName("POST - /orders/{orderId}/products")
  inner class AddOrderProduct {

    private val existingOrderUrl = fromPath("/orders")
      .path("/{orderId}/products")
      .buildAndExpand(hashMapOf("orderId" to "ord-4e1dc2ac-debc-11ec-9d64-0242ac120002"))
      .toString()

    @Test
    fun `should add a product to an order`() {
      val request = AddOrderProductRequest(productId = "prd-c6064f50-dc73-11ec-9d64-0242ac120002", quantity = 2)

      val url = fromPath("/orders")
        .path("/{orderId}/products")
        .buildAndExpand(hashMapOf("orderId" to "ord-28014332-dec1-11ec-9d64-0242ac120002"))
        .toString()

      val response = restTemplate.postForEntity(url, request, OrderResponse::class.java)
      val order = response.body!!

      assertEquals(HttpStatus.OK, response.statusCode)

      assertEquals("ord-28014332-dec1-11ec-9d64-0242ac120002", order.id)
      assertEquals(2, order.products.size)
      assertEquals("7.50", order.products.first().totalPrice)
      assertEquals("6.00", order.products[1].totalPrice)
      assertEquals(2, order.products[1].quantity)
      assertEquals("13.50", order.totalPrice)
    }

    @Test
    fun `should increment the quantity of an existing order product`() {
      val request = AddOrderProductRequest(productId = "prd-95956b62-dc73-11ec-9d64-0242ac120002", quantity = 1)

      val response = restTemplate.postForEntity(existingOrderUrl, request, OrderResponse::class.java)
      val order = response.body!!

      assertEquals(HttpStatus.OK, response.statusCode)

      assertEquals("ord-4e1dc2ac-debc-11ec-9d64-0242ac120002", order.id)
      assertEquals(1, order.products.size)
      assertEquals(2, order.products.first().quantity)
      assertEquals("15.00", order.products.first().totalPrice)
      assertEquals("15.00", order.totalPrice)
    }

    @Test
    fun `should throw an exception if the order does not exist`() {
      val request = AddOrderProductRequest(productId = "prd-95956b62-dc73-11ec-9d64-0242ac120002", quantity = 1)

      val url = fromPath("/orders")
        .path("/{orderId}/products")
        .buildAndExpand(hashMapOf("orderId" to "ord-4"))
        .toString()

      val response = restTemplate.postForEntity(url, request, ErrorResponse::class.java)

      val expectedResponse = ErrorResponse(
        "RESOURCE_NOT_FOUND",
        "Not found",
        "The requested resource could not be found"
      )

      assertNotNull(response)
      assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
      assertEquals(expectedResponse, response.body)
    }

    @Test
    fun `should throw an exception if the product does not exist`() {
      val request = AddOrderProductRequest(productId = "prd-9", quantity = 1)

      val response = restTemplate.postForEntity(existingOrderUrl, request, ErrorResponse::class.java)

      val expectedResponse = ErrorResponse(
        "RESOURCE_NOT_FOUND",
        "Not found",
        "The requested resource could not be found"
      )

      assertNotNull(response)
      assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
      assertEquals(expectedResponse, response.body)
    }

    @Test
    fun `should throw an exception if the product does not have enough stock`() {
      val request = AddOrderProductRequest(productId = "prd-95956b62-dc73-11ec-9d64-0242ac120002", quantity = 6)

      val response = restTemplate.postForEntity(existingOrderUrl, request, ErrorResponse::class.java)

      val expectedResponse = ErrorResponse(
        "BAD_REQUEST",
        "Bad request",
        "Bad request"
      )

      assertNotNull(response)
      assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
      assertEquals(expectedResponse, response.body)
    }
  }
}