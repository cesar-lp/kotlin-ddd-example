package com.example.ddd.product.domain.useCases

import com.example.ddd.product.domain.models.entities.Product
import com.example.ddd.product.domain.repositories.ProductRepository
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
    val product = getProduct(id)

    product.updateStatus(status)

    return repository.save(product)
  }
}