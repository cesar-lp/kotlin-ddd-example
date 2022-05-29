package com.example.ddd.order.application.controllers

import com.example.ddd.common.application.errors.ErrorResponse
import com.example.ddd.order.application.controllers.responses.ReportResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext

class ClientControllerTest : BaseControllerTest() {

  @Nested
  @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
  @DisplayName("GET - /clients/{clientId}/report")
  inner class GetClientReport {

    @Test
    fun `should get client report`() {
      val clientId = "cli-79a43474-decb-11ec-9d64-0242ac120002"

      val response = restTemplate.getForEntity("/clients/${clientId}/report", ReportResponse::class.java)
      val report = response.body!!

      assertEquals(HttpStatus.OK, response.statusCode)
      assertEquals("John Doe", report.fullName)
      assertEquals(2, report.orders.size)
    }

    @Test
    fun `should throw an exception if the client is not found`() {
      val response = restTemplate.getForEntity("/clients/cli-1/report", ErrorResponse::class.java)

      val expectedError = ErrorResponse(
        "RESOURCE_NOT_FOUND",
        "Not found",
        "The requested resource could not be found"
      )

      assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
      assertEquals(expectedError, response.body)
    }

  }

}