package com.example.userapi.service

import com.example.userapi.model.NewUser
import com.example.userapi.model.User
import com.example.userapi.repository.UserEntity

object UserMapper {

    fun toEntity(newUser: NewUser): UserEntity =
        UserEntity(
            name = newUser.name,
            email = newUser.email
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