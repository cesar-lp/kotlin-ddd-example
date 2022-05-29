package com.example.ddd.order.domain.useCases.product

import com.example.ddd.order.domain.models.entities.Product
import com.example.ddd.order.domain.repositories.ProductRepository
import org.springframework.stereotype.Service

interface ChangeProductStatusUseCase {
  operator fun invoke(id: String, status: String): Product
}

@Service
class ChangeProductStatusUseCaseImpl(
  private val getProduct: GetProductUseCase,
  private val repository: ProductRepository
) : ChangeProductStatusUseCase {

  override fun invoke(id: String, status: String): Product {
    return getProduct(id).let {
      it.updateStatus(status)
      repository.save(it)
    }
  }
}