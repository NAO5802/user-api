package com.example.userapi.service

import com.example.userapi.exception.AccessDeniedException
import com.example.userapi.exception.TaskNotFoundException
import com.example.userapi.model.CreateTaskRequest
import com.example.userapi.model.Task
import com.example.userapi.model.UpdateTaskRequest
import com.example.userapi.repository.TaskRepository
import org.springframework.stereotype.Service


@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val userService: UserService
) {

    fun getTasks(userId: Long): List<Task> =
        userService.findByIdOrThrow(userId)
        .let { user -> taskRepository.findAll(user.id) }


    fun getTaskById(userId: Long, taskId: Long): Task {
        userService.findByIdOrThrow(userId)
        val task = taskRepository.findById(taskId) ?: throw TaskNotFoundException("タスクが存在しません")
            if(task.userId != userId) throw AccessDeniedException("アクセス権限がありません")
            return task
    }

    fun createTask(userId: Long, request: CreateTaskRequest): Task {
        userService.findByIdOrThrow(userId)
        return taskRepository.save(userId, request.title, request.description)
    }

    fun updateTask(userId: Long, taskId: Long, request: UpdateTaskRequest): Task {
        userService.findByIdOrThrow(userId)
        val found = taskRepository.findById(taskId) ?: throw TaskNotFoundException("タスクが存在しません")
        if(found.userId != userId) throw AccessDeniedException("アクセス権限がありません")

        val updated = found.copy(
            title = request.title ?: found.title,
            description =  request.description ?: found.description,
            status = request.status ?: found.status
        )
        return taskRepository.update(updated)
    }

    fun deleteTask(userId: Long, taskId: Long): Unit {
        userService.findByIdOrThrow(userId)
        val found = taskRepository.findById(taskId) ?: throw TaskNotFoundException("タスクが存在しません")
        if(found.userId != userId) throw AccessDeniedException("アクセス権限がありません")
        taskRepository.delete(taskId)
    }


}