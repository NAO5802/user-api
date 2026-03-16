package com.example.userapi.model

data class User(
    val id: Long,
    val name: String,
    val email: String
)

data class CreateUserRequest(
    val name: String,
    val email: String
)
