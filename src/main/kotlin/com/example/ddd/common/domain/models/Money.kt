package com.example.ddd.common.domain.models

import java.math.BigDecimal
import java.util.*

class Money private constructor(private val value: BigDecimal, private val currency: Currency) {

  companion object {

    fun of(value: String) = Money(BigDecimal(value), Currency.USD)

    fun of(value: BigDecimal) = Money(value, Currency.USD)

    fun ZERO() = Money(BigDecimal.ZERO, Currency.USD)

  }

  operator fun plus(other: Money) = Money(value.plus(other.value), currency)

  operator fun minus(other: Money) = Money(value.minus(other.value), currency)

  operator fun times(quantity: Int) = Money(value.times(quantity.toBigDecimal()), currency)

  fun getValue() = value.toString()

  fun isZeroOrNegative() = value <= BigDecimal.ZERO

  override fun toString(): String {
    return "Money(${value}, ${currency})"
  }

  override fun hashCode() = Objects.hash(value, currency)

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Money) return false

    return (other.value == value && other.currency == currency)
  }

}