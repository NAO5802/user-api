package com.example.userapi.service

import com.example.userapi.model.NewTask
import com.example.userapi.model.Task
import com.example.userapi.repository.TaskEntity

object TaskMapper {

    fun toEntity(newTask: NewTask): TaskEntity =
        TaskEntity(
            userId = newTask.userId,
            title = newTask.title,
            description = newTask.description,
            status = newTask.status,
        )

    fun toEntity(task: Task): TaskEntity =
        TaskEntity(
            id = task.id,
            userId = task.userId,
            title = task.title,
            description = task.description,
            status = task.status,
            createdAt = task.createdAt
        )

    fun toDomain(entity: TaskEntity): Task =
        Task(
            id = entity.id,
            userId = entity.userId,
            title = entity.title,
            description = entity.description,
            status = entity.status,
            createdAt = entity.createdAt
        )

}