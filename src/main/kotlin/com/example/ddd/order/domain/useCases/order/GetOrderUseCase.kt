package com.example.ddd.order.domain.useCases.order

import com.example.ddd.order.domain.errors.order.OrderNotFoundException
import com.example.ddd.order.domain.models.entities.Order
import com.example.ddd.order.domain.repositories.OrderRepository
import org.springframework.stereotype.Service

interface GetOrderUseCase {
  operator fun invoke(id: String): Order
}

@Service
class GetOrderUseCaseImpl(private val repository: OrderRepository) : GetOrderUseCase {

  override fun invoke(id: String): Order {
    return repository.get(id) ?: throw OrderNotFoundException(id)
  }
}