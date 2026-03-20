package com.example.userapi.repository

import com.example.userapi.exception.TaskNotFoundException
import com.example.userapi.model.Task
import com.example.userapi.model.TaskFilter
import com.example.userapi.model.TaskStatus
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicLong


@Repository
class TaskRepository {
    private val tasks = mutableListOf<Task>()
    private val idCounter = AtomicLong(1)

    fun findAll(userId: Long, filter: TaskFilter): List<Task> =
        tasks.filter { task ->
            val isValidUser = task.userId == userId
            val isContainsTitle = filter.title?.let{ task.title.contains(it)}?: true
            val matchesStatus = filter.status?.let { task.status == it }?: true

            isValidUser && isContainsTitle && matchesStatus
        }

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

    fun update(updated: Task): Task {
        val index = tasks.indexOfFirst { it.id == updated.id }
        if(index == -1) throw TaskNotFoundException("タスクが存在しません")
        tasks[index] = updated
        return updated
    }

    fun delete(taskId: Long): Unit {
        val isRemoved = tasks.removeIf { it.id == taskId }
        if(!isRemoved) throw TaskNotFoundException("タスクが存在しません")
    }
}