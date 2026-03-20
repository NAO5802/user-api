package com.example.userapi.service

import com.example.userapi.exception.UserNotFoundException
import com.example.userapi.model.CreateUserRequest
import com.example.userapi.model.User
import com.example.userapi.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun getAllUsers(): List<User> = userRepository.findAll()

    fun getUserById(id: Long): User? = userRepository.findById(id)

    fun createUser(request: CreateUserRequest): User {
        require(request.name.isNotBlank()){"Name must not be blank"}
        require(request.email.isNotBlank()){"Email must not be blank"}
        require(!userRepository.existsByEmail(request.email)){"Email already exists"}
        val user = userRepository.save(request.name,request.email)
        return user
    }

     fun findByIdOrThrow(userId: Long): User {
        return this.getUserById(userId) ?: throw UserNotFoundException("ユーザーが存在しません")
    }
}
