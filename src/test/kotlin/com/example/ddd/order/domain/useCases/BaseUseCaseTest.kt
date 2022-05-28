package com.example.ddd.order.domain.useCases

import com.example.ddd.common.domain.models.ID
import com.example.ddd.common.domain.models.Money
import com.example.ddd.order.domain.models.entities.Product
import com.example.ddd.order.domain.repositories.OrderRepository
import com.example.ddd.order.domain.repositories.ProductRepository
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal

@ExtendWith(MockKExtension::class)
open class BaseUseCaseTest {

  @MockK
  protected lateinit var productRepository: ProductRepository

  @MockK
  protected lateinit var orderRepository: OrderRepository

  protected val beerProduct = Product(
    id = ID.of("prd-8f6f04dc-dc73-11ec-9d64-0242ac120002"),
    name = "Beer",
    description = "Enjoy your day with a nice cold beer",
    stock = 10,
    price = Money.of(BigDecimal("2.50"))
  )

  protected val STEAK = Product(
    id = ID.of("prd-95956b62-dc73-11ec-9d64-0242ac120002"),
    name = "Steak",
    description = "Big steak",
    stock = 5,
    price = Money.of(BigDecimal("7.50"))
  )

  protected val CHIPS = Product(
    id = ID.of("prd-c6064f50-dc73-11ec-9d64-0242ac120002"),
    name = "Chips",
    description = "The best chips in town",
    stock = 20,
    price = Money.of(BigDecimal("3.00"))
  )
}