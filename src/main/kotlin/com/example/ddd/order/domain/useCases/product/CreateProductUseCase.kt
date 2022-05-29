package com.example.ddd.order.domain.useCases.product

import com.example.ddd.common.domain.models.Money
import com.example.ddd.order.domain.models.NewProduct
import com.example.ddd.order.domain.models.entities.Product
import com.example.ddd.order.domain.repositories.ProductRepository
import org.springframework.stereotype.Service

interface CreateProductUseCase {
  operator fun invoke(newProduct: NewProduct): Product
}

@Service
class CreateProductUseCaseImpl(
  private val repository: ProductRepository
) : CreateProductUseCase {

  override fun invoke(newProduct: NewProduct): Product {
    val product = Product(
      name = newProduct.name,
      description = newProduct.description,
      price = Money.of(newProduct.price)
    )

    return repository.save(product)
  }

}