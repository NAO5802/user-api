package com.example.userapi.dto

import com.example.userapi.model.TaskStatus
import com.example.userapi.repository.TaskEntity
import jakarta.validation.constraints.NotBlank

data class CreateTaskRequest(
    @field:NotBlank val title: String,
    @field:NotBlank val description: String
)

fun CreateTaskRequest.toEntity(userId: Long): TaskEntity =
    TaskEntity(
        userId = userId,
        title = this.title,
        description = this.description,
        status = TaskStatus.TODO,
    )