package com.example.userapi.model

import com.example.userapi.repository.UserEntity
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

fun CreateUserRequest.toEntity(): UserEntity =
    UserEntity(
        name = this.name,
        email = this.email
    )
