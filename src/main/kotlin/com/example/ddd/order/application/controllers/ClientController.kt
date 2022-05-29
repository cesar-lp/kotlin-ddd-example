package com.example.ddd.order.application.controllers

import com.example.ddd.order.application.controllers.presenters.ReportPresenter
import com.example.ddd.order.application.controllers.responses.ReportResponse
import com.example.ddd.order.domain.useCases.GetReportUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/clients")
class ClientController(
  private val getReport: GetReportUseCase,
  private val present: ReportPresenter
) {

  @GetMapping("/{id}/report")
  fun getClientReport(@PathVariable id: String): ReportResponse {
    return present(getReport(id))
  }
}