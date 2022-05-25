package com.example.ddd.order.application.controllers

import com.example.ddd.App
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate

@SpringBootTest(
  classes = [App::class],
  webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class BaseControllerTest {

  @Autowired
  protected lateinit var restTemplate: TestRestTemplate
}