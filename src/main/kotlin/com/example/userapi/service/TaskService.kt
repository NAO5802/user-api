package com.example.userapi.service

import com.example.userapi.exception.AccessDeniedException
import com.example.userapi.exception.TaskNotFoundException
import com.example.userapi.exception.UserNotFoundException
import com.example.userapi.model.Task
import com.example.userapi.repository.TaskRepository
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

    fun getTaskById(userId: Long, taskId: Long): Task {
            userService.getUserById(userId) ?: throw UserNotFoundException("ユーザーが存在しません")
            val task = taskRepository.findById(taskId) ?: throw TaskNotFoundException("タスクが存在しません")
            if(task.userId != userId) throw AccessDeniedException("アクセス権限がありません")
            return task
    }



}