package com.example.userapi.controller

import com.example.userapi.model.CreateTaskRequest
import com.example.userapi.model.Task
import com.example.userapi.model.TaskFilter
import com.example.userapi.model.UpdateTaskRequest
import com.example.userapi.service.TaskService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users/{userId}/tasks")
class TaskController(private val taskService: TaskService) {

    @GetMapping
    fun getTasks(@PathVariable userId: Long, @RequestParam(required = false) title: String?, @RequestParam(required = false) status: String?): List<Task> {
        return TaskFilter.of(title,status)
            .let { filter -> taskService.getTasks(userId, filter) }
    }

    @GetMapping("/{taskId}")
    fun getTaskById(@PathVariable userId: Long ,@PathVariable taskId: Long): ResponseEntity<Task> =
        taskService.getTaskById(userId, taskId).let{ ResponseEntity.ok(it) }

    @PostMapping
    fun createTask(@PathVariable userId: Long, @Valid @RequestBody request: CreateTaskRequest): ResponseEntity<Task> =
        taskService.createTask(userId, request).let { ResponseEntity.status(HttpStatus.CREATED).body(it) }

    @PutMapping("/{taskId}")
    fun updateTask(@PathVariable userId: Long, @PathVariable taskId: Long, @RequestBody request: UpdateTaskRequest): ResponseEntity<Task> =
        taskService.updateTask(userId, taskId, request).let { ResponseEntity.ok(it) }

    @DeleteMapping("/{taskId}")
    fun deleteTask(@PathVariable userId: Long, @PathVariable taskId: Long): ResponseEntity<Unit> =
        taskService.deleteTask(userId, taskId).let { ResponseEntity.status(HttpStatus.NO_CONTENT).build() }
}