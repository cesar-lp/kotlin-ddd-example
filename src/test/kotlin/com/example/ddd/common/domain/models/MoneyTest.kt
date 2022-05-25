package com.example.ddd.common.domain.models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class MoneyTest {

  @Test
  fun `should add one money to another`() {
    val moneyOne = Money.of("1.25")
    val moneyTwo = Money.of("0.75")

    assertEquals("2.00", (moneyOne + moneyTwo).getValue())
  }

  @Test
  fun `should multiply one money n times`() {
    val money = Money.of("3.50")
    val times = 3

    assertEquals("10.50", (money * times).getValue())
  }

  @Test
  fun `should validate that a money value is negative or zero`() {
    assertTrue(Money.of("0").isZeroOrNegative())
    assertTrue(Money.of("-1").isZeroOrNegative())
  }
}