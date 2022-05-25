package com.example.ddd.order.domain.useCases

import com.example.ddd.common.domain.models.Money
import com.example.ddd.order.domain.models.UpdatedProduct
import com.example.ddd.order.domain.models.entities.Product
import com.example.ddd.order.domain.repositories.ProductRepository
import org.springframework.stereotype.Service
import java.time.Instant

interface UpdateProductUseCase {
  operator fun invoke(id: String, updatedProduct: UpdatedProduct): Product
}

@Service
class UpdateProductUseCaseImpl(
  private val getProduct: GetProductUseCase,
  private val repository: ProductRepository
) : UpdateProductUseCase {

  override fun invoke(id: String, updatedProduct: UpdatedProduct): Product {
    val product = getProduct(id).apply {
      name = updatedProduct.name
      description = updatedProduct.description
      updatePrice(Money.of(updatedProduct.price))
      updatedAt = Instant.now()
    }

    return repository.save(product)
  }
}