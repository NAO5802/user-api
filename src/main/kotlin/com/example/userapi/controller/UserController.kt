package com.example.userapi.controller

import com.example.userapi.model.CreateUserRequest
import com.example.userapi.model.User
import com.example.userapi.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @GetMapping
    fun getAllUsers(): List<User> = userService.getAllUsers()

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<User> {
        val user = userService.getUserById(id)
        return if (user != null) ResponseEntity.ok(user)
        else ResponseEntity.notFound().build()
    }

    @PostMapping
    fun createUser(@RequestBody request: CreateUserRequest): ResponseEntity<Any> {
        return try {
            val user = userService.createUser(request)
            ResponseEntity.status(HttpStatus.CREATED).body(user)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }
}
