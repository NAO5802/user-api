package com.example.userapi.repository

import com.example.userapi.model.Task
import org.springframework.stereotype.Repository

@Repository
class TaskRepository {
    private val tasks = mutableListOf<Task>()

    fun findAll(userId: Long): List<Task> = tasks.filter { it.userId == userId }

}