package com.example.userapi.service

import com.example.userapi.exception.AccessDeniedException
import com.example.userapi.exception.TaskNotFoundException
import com.example.userapi.model.CreateTaskRequest
import com.example.userapi.model.Task
import com.example.userapi.model.TaskFilter
import com.example.userapi.model.UpdateTaskRequest
import com.example.userapi.model.toEntity
import com.example.userapi.repository.TaskRepository
import com.example.userapi.repository.toDomain
import org.springframework.stereotype.Service


@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val userService: UserService
) {

    fun getTasks(userId: Long, filter: TaskFilter): List<Task> =
        userService.getUserById(userId)
            .let{emptyList()}
    //  TODO
//        .let { user -> taskRepository.findAll(user.id, filter) }

    fun getTaskById(userId: Long, taskId: Long): Task {
        userService.getUserById(userId)
        val task = taskRepository.findById(taskId)
            .orElseThrow { TaskNotFoundException("タスクが存在しません") }
            .toDomain()
        if(task.userId != userId) throw AccessDeniedException("アクセス権限がありません")

        return task
    }

    fun createTask(userId: Long, request: CreateTaskRequest): Task {
        userService.getUserById(userId)
        return taskRepository.save(request.toEntity(userId)).toDomain()
    }

    fun updateTask(userId: Long, taskId: Long, request: UpdateTaskRequest): Task {
        userService.getUserById(userId)
        val found = taskRepository.findById(taskId)
            .orElseThrow{TaskNotFoundException("タスクが存在しません")}
            .toDomain()
        if(found.userId != userId) throw AccessDeniedException("アクセス権限がありません")

        val updated = found.copy(
            title = request.title ?: found.title,
            description =  request.description ?: found.description,
            status = request.status ?: found.status
        )
        return taskRepository.save(updated.toEntity()).toDomain()
    }

    fun deleteTask(userId: Long, taskId: Long): Unit {
        userService.getUserById(userId)
        val found = taskRepository.findById(taskId)
            .orElseThrow{ TaskNotFoundException("タスクが存在しません") }
            .toDomain()
        if(found.userId != userId) throw AccessDeniedException("アクセス権限がありません")

        taskRepository.delete(found.toEntity())
    }


}