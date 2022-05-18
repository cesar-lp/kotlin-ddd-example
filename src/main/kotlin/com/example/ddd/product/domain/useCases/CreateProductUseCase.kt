package com.example.ddd.product.domain.useCases

import com.example.ddd.product.domain.models.NewProduct
import com.example.ddd.product.domain.models.entities.Product
import com.example.ddd.product.domain.repositories.ProductRepository
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
      description = newProduct.description
    )

    return repository.save(product)
  }

}