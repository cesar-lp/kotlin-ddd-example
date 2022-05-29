package com.example.ddd.order.application.controllers.presenters

import com.example.ddd.order.application.controllers.responses.ReportResponse
import com.example.ddd.order.domain.models.ClientOrders
import org.springframework.stereotype.Component

interface ReportPresenter {
  operator fun invoke(clientOrders: ClientOrders): ReportResponse
}

@Component
class ReportPresenterImpl(
  private val presentOrder: OrderPresenter
) : ReportPresenter {

  override fun invoke(clientOrders: ClientOrders): ReportResponse {
    return ReportResponse(
      fullName = clientOrders.client.fullName,
      orders = clientOrders.orders.map { presentOrder(it) }
    )
  }

}