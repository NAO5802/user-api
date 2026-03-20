package com.example.userapi.controller

import com.example.userapi.exception.AccessDeniedException
import com.example.userapi.exception.TaskNotFoundException
import com.example.userapi.exception.UserNotFoundException
import com.example.userapi.model.CreateTaskRequest
import com.example.userapi.model.Task
import com.example.userapi.model.UpdateTaskRequest
import com.example.userapi.service.TaskService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users/{userId}/tasks")
class TaskController(private val taskService: TaskService) {

    @GetMapping
    fun getTasks(@PathVariable userId: Long): List<Task> = taskService.getTasks(userId)

    @GetMapping("/{taskId}")
    fun getTaskById(@PathVariable userId: Long ,@PathVariable taskId: Long): ResponseEntity<Task> =
        taskService.getTaskById(userId, taskId).let{ ResponseEntity.ok(it) }

    @PostMapping
    fun createTask(@PathVariable userId: Long, @RequestBody request: CreateTaskRequest): ResponseEntity<Task> =
        taskService.createTask(userId, request).let { ResponseEntity.status(HttpStatus.CREATED).body(it) }

    @PutMapping("/{taskId}")
    fun updateTask(@PathVariable userId: Long, @PathVariable taskId: Long, @RequestBody request: UpdateTaskRequest): ResponseEntity<Task> =
        taskService.updateTask(userId, taskId, request).let { ResponseEntity.ok(it) }

    @DeleteMapping("/{taskId}")
    fun deleteTask(@PathVariable userId: Long, @PathVariable taskId: Long): ResponseEntity<Unit> =
        taskService.deleteTask(userId, taskId).let { ResponseEntity.status(HttpStatus.NO_CONTENT).build() }

    // TODO: UserControllerの例外とともに専用クラスに切り出す
    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFound(e: UserNotFoundException): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)

    @ExceptionHandler(TaskNotFoundException::class)
    fun handleTaskNotFound(e: TaskNotFoundException): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDenied(e: AccessDeniedException): ResponseEntity<String> =
        ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.message)
}