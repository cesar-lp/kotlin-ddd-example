package com.example.ddd.common.application.errors

data class ErrorDetail(
    val code: String,
    val title: String,
    val detail: String
)