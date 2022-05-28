package com.example.ddd.order.domain.models.entities

import com.example.ddd.common.domain.models.ID
import java.time.Instant

class Client(
  val id: String = ID.generate("cli"),
  val fullName: String,
  val createdAt: Instant = Instant.now(),
  val updatedAt: Instant = Instant.now()
)