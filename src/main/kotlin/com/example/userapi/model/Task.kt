package com.example.userapi.model

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

data class NewTask(
    val userId: Long,
    val title: String,
    val description: String,
    val status: TaskStatus,
)

enum class TaskStatus {
    TODO, IN_PROGRESS, DONE
}

data class CreateTaskRequest(
    @field:NotBlank val title: String,
    @field:NotBlank val description: String
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