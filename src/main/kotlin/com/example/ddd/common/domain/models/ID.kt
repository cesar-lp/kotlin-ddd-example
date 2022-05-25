package com.example.ddd.common.domain.models

import java.util.*

class ID private constructor(private val value: String) {

  companion object {

    fun generate(prefix: String) = ID("${prefix}-${UUID.randomUUID()}").toString()

    fun of(value: String) = ID(value).toString()

  }

  override fun toString() = value

}