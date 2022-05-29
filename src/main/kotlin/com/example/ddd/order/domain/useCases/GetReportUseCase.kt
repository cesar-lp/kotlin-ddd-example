package com.example.ddd.order.domain.useCases

import com.example.ddd.order.domain.models.ClientOrders
import com.example.ddd.order.domain.repositories.OrderRepository
import org.springframework.stereotype.Service

interface GetReportUseCase {
  operator fun invoke(id: String): ClientOrders
}

@Service
class GetReportUseCaseimpl(
  private val getClient: GetClientUseCase,
  private val orderRepository: OrderRepository
) : GetReportUseCase {

  override fun invoke(id: String): ClientOrders {
    val client = getClient(id)
    val orders = orderRepository.getByClient(id)

    return ClientOrders(client, orders)
  }

}