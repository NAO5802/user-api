package com.example.userapi.service

import com.example.userapi.model.Task
import com.example.userapi.repository.TaskRepository
import com.example.userapi.exception.UserNotFoundException
import org.springframework.stereotype.Service

@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val userService: UserService
) {

    fun getTasks(userId: Long): List<Task> =
        userService.getUserById(userId)
        ?.let { user -> taskRepository.findAll(user.id) }
            ?: throw UserNotFoundException("ユーザーが存在しません")

}