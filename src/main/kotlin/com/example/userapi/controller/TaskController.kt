package com.example.userapi.controller

import com.example.userapi.exception.UserNotFoundException
import com.example.userapi.model.Task
import com.example.userapi.service.TaskService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users/{userId}/tasks")
class TaskController(private val taskService: TaskService) {

    @GetMapping
    fun getTasks(@PathVariable userId: Long): List<Task> = taskService.getTasks(userId)

    // TODO: UserControllerの例外とともに専用クラスに切り出す
    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFound(e: UserNotFoundException): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
}