package com.example.userapi.repository

import com.example.userapi.model.User
import org.springframework.stereotype.Repository
import java.util.concurrent.atomic.AtomicLong

@Repository
class UserRepository {
    private val users = mutableListOf<User>()
    private val idCounter = AtomicLong(1)

    fun findAll(): List<User> = users.toList()

    fun findById(id: Long): User? = users.find { it.id == id }

    fun save(name: String, email: String): User {
        val user = User(id = idCounter.getAndIncrement(), name = name, email = email)
        users.add(user)
        return user
    }

    fun existsByEmail(email: String): Boolean = users.any { it.email == email }
}
