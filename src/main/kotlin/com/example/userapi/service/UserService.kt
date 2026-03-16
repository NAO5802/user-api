package com.example.userapi.service

import com.example.userapi.model.CreateUserRequest
import com.example.userapi.model.User
import com.example.userapi.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun getAllUsers(): List<User> = userRepository.findAll()

    fun getUserById(id: Long): User? = userRepository.findById(id)

    // TODO(human): createUser関数を実装してください
    // - nameとemailが空でないかチェック
    // - emailが重複していないかチェック（existsByEmail使用）
    // - バリデーションNGの場合はIllegalArgumentExceptionを投げる
    // - OKならuserRepository.save(name, email)を呼んでUserを返す
    fun createUser(request: CreateUserRequest): User {
        require(request.name.isNotBlank()){"Name must not be blank"}
        require(request.email.isNotBlank()){"Email must not be blank"}
        require(!userRepository.existsByEmail(request.email)){"Email already exists"}
        val user = userRepository.save(request.name,request.email)
        return user
    }
}
