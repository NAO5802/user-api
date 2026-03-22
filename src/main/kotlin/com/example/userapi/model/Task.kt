package com.example.userapi.model

import com.example.userapi.repository.TaskEntity
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

data class Task(
    val id: Long,
    val userId: Long,
    val title: String,
    val description: String,
    val status: TaskStatus,
    val createdAt: LocalDateTime
)

fun Task.toEntity(): TaskEntity =
    TaskEntity(
        id = this.id,
        userId = this.userId,
        title = this.title,
        description = this.description,
        status = this.status,
        createdAt = this.createdAt
    )

enum class TaskStatus {
    TODO, IN_PROGRESS, DONE
}

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

data class UpdateTaskRequest(val title: String?, val description: String?, val status: TaskStatus?)

@ConsistentCopyVisibility
data class TaskFilter private constructor(val title: String?, val status: TaskStatus?) {
    companion object {
        fun of(title: String?, status: String?) =
            TaskFilter(
                title,
                status?.let{
                    TaskStatus.entries.find { entry -> entry.name == it  }
                        ?: throw IllegalArgumentException("不正なステータスです")
                }
            )

    }
}