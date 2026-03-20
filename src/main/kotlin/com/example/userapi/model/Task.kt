package com.example.userapi.model

import java.time.LocalDateTime

data class Task(
    val id: Long,
    val userId: Long,
    val title: String,
    val description: String,
    val status: TaskStatus,
    val createdAt: LocalDateTime
) {
}

enum class TaskStatus {
    TODO, IN_PROGRESS, DONE
}

data class CreateTaskRequest(val title: String, val description: String)
data class UpdateTaskRequest(val title: String?, val description: String?, val status: TaskStatus?)