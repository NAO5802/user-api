package com.example.userapi.controller

import com.example.userapi.model.CreateUserRequest
import com.example.userapi.model.User
import com.example.userapi.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @GetMapping
    fun getAllUsers(): List<User> = userService.getAllUsers()

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<User> =
        userService.getUserById(id).let{ ResponseEntity.ok(it) }

    @PostMapping
    fun createUser(@Valid @RequestBody request: CreateUserRequest): ResponseEntity<User> =
        userService.createUser(request).let { ResponseEntity.status(HttpStatus.CREATED).body(it) }
}
