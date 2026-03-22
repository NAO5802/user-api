package com.example.userapi.service

import com.example.userapi.exception.UserNotFoundException
import com.example.userapi.model.CreateUserRequest
import com.example.userapi.model.User
import com.example.userapi.model.toEntity
import com.example.userapi.repository.UserRepository
import com.example.userapi.repository.toDomain
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun getAllUsers(): List<User> = userRepository.findAll().map { it.toDomain() }

    fun getUserById(id: Long): User =
        userRepository.findById(id)
        .orElseThrow{ UserNotFoundException("ユーザーが存在しません") }
        .toDomain()


    fun createUser(request: CreateUserRequest): User {
        val isUsedEmail = userRepository.findByEmail(request.email) != null
        if(isUsedEmail) throw IllegalArgumentException("Emailがすでに存在します")

        return userRepository.save(request.toEntity()).toDomain()
    }

}
