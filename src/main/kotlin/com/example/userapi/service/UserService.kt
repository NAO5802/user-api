package com.example.userapi.service

import com.example.userapi.exception.UserNotFoundException
import com.example.userapi.model.CreateUserRequest
import com.example.userapi.model.User
import com.example.userapi.repository.UserRepository
import com.example.userapi.service.UserMapper.toDomain
import com.example.userapi.service.UserMapper.toEntity
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun getAllUsers(): List<User> = userRepository.findAll().map { toDomain(it) }

    fun getUserById(id: Long): User =
        userRepository.findById(id)
            .orElseThrow{ UserNotFoundException("ユーザーが存在しません") }
            .let { toDomain(it) }


    fun createUser(request: CreateUserRequest): User {
        val isUsedEmail = userRepository.findByEmail(request.email) != null
        if(isUsedEmail) throw IllegalArgumentException("Emailがすでに存在します")

        val user = userRepository.save(toEntity(request))
        return toDomain(user)
    }

}
