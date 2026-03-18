package com.example.userapi.repository

import com.example.userapi.model.Task
import com.example.userapi.model.TaskStatus
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicLong


@Repository
class TaskRepository {
    private val tasks = mutableListOf<Task>()
    private val idCounter = AtomicLong(1)

    fun findAll(userId: Long): List<Task> = tasks.filter { it.userId == userId }
    fun findById(taskId: Long): Task? = tasks.firstOrNull { it.id == taskId }

    fun save(userId: Long, title: String, description: String): Task {
        // TODO: JPAを使用する実装に変更する際、Entityの組み立てをServiceで行う
        val newTask = Task(
            id = idCounter.getAndIncrement(),
            userId = userId,
            title = title,
            description = description,
            status = TaskStatus.TODO,
            createdAt = LocalDateTime.now()
        )

        tasks.add(newTask)
        return newTask
    }
}