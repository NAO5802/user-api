package com.example.userapi.model

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class User(
    val id: Long,
    val name: String,
    val email: String
)

data class CreateUserRequest(
    @field:NotBlank val name: String,
    @field:NotBlank @field:Email val email: String
)
