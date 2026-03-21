package com.example.userapi.service

import com.example.userapi.model.CreateUserRequest
import com.example.userapi.model.User
import com.example.userapi.repository.UserEntity

object UserMapper {

    fun toEntity(request: CreateUserRequest): UserEntity =
        UserEntity(
            name = request.name,
            email = request.email
        )

    fun toEntity(user: User): UserEntity =
        UserEntity(
            id = user.id,
            name = user.name,
            email = user.email
        )

    fun toDomain(entity: UserEntity): User =
        User(
            id = entity.id,
            name = entity.name,
            email = entity.email
        )
}