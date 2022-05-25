package com.example.ddd.order.application.controllers

import com.example.ddd.common.application.errors.ErrorResponse
import com.example.ddd.order.application.controllers.requests.CreateOrderRequest
import com.example.ddd.order.application.controllers.requests.OrderProductLineItemRequest
import com.example.ddd.order.application.controllers.responses.OrderProductResponse
import com.example.ddd.order.application.controllers.responses.OrderResponse
import com.example.ddd.testUtils.deserializeJSON
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.annotation.DirtiesContext

class OrderControllerTest : BaseControllerTest() {

  @Nested
  @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
  @DisplayName("POST - /orders")
  inner class CreateOrder {

    @Test
    fun `should create an order`() {
      val jsonResponseFilePath = "/controllers/order/createOrder.json"
      val request = CreateOrderRequest(
        listOf(
          OrderProductLineItemRequest("prd-8f6f04dc-dc73-11ec-9d64-0242ac120002", 2),
          OrderProductLineItemRequest("prd-95956b62-dc73-11ec-9d64-0242ac120002", 4)
        )
      )

      val response = restTemplate.postForEntity("/orders", request, OrderResponse::class.java)
      val expectedResponse = cleanResponse(response, jsonResponseFilePath)

      assertNotNull(response)
      assertEquals(HttpStatus.CREATED, response.statusCode)
      assertEquals(expectedResponse, response.body)
    }

    @Test
    fun `should fail to create an order for non existing products`() {
      val request = CreateOrderRequest(
        listOf(
          OrderProductLineItemRequest("prd-1", 2),
          OrderProductLineItemRequest("prd-99", 4)
        )
      )

      val response = restTemplate.postForEntity("/orders", request, ErrorResponse::class.java)

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
    fun `should fail to create an order with more quantity than stock available for a product`() {
      val request = CreateOrderRequest(
        listOf(
          OrderProductLineItemRequest("prd-8f6f04dc-dc73-11ec-9d64-0242ac120002", 20),
          OrderProductLineItemRequest("prd-95956b62-dc73-11ec-9d64-0242ac120002", 4)
        )
      )

      val response = restTemplate.postForEntity("/orders", request, ErrorResponse::class.java)

      val expectedResponse = ErrorResponse(
        "BAD_REQUEST",
        "Bad request",
        "Bad request"
      )

      assertNotNull(response)
      assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
      assertEquals(expectedResponse, response.body)
    }

    private fun cleanResponse(response: ResponseEntity<OrderResponse>, jsonResponseFilePath: String): OrderResponse {
      val beerOrderProduct =
        response.body!!.products.first { it.productId == "prd-8f6f04dc-dc73-11ec-9d64-0242ac120002" }
      val steakOrderProduct =
        response.body!!.products.first { it.productId == "prd-95956b62-dc73-11ec-9d64-0242ac120002" }

      val expectedJsonResponse = deserializeJSON<OrderResponse>(jsonResponseFilePath)

      val tempProductList = mutableListOf<OrderProductResponse>()

      expectedJsonResponse.products.forEach {
        if (it.productId == beerOrderProduct.productId) {
          tempProductList.add(it.copy(id = beerOrderProduct.id))
        } else {
          tempProductList.add(it.copy(steakOrderProduct.id))
        }
      }

      return expectedJsonResponse.copy(
        id = response.body?.id.toString(),
        products = tempProductList,
        createdAt = response.body?.createdAt.toString(),
        updatedAt = response.body?.updatedAt.toString()
      )
    }
  }
}