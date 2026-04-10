package com.example.userapi.service

import com.example.userapi.dto.CreateTaskRequest
import com.example.userapi.dto.PagedResponse
import com.example.userapi.dto.UpdateTaskRequest
import com.example.userapi.dto.toEntity
import com.example.userapi.exception.AccessDeniedException
import com.example.userapi.exception.TaskNotFoundException
import com.example.userapi.model.Task
import com.example.userapi.model.TaskFilter
import com.example.userapi.model.toEntity
import com.example.userapi.repository.TaskRepository
import com.example.userapi.repository.toDomain
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service


@Service
class TaskService(
    private val taskRepository: TaskRepository,
    private val userService: UserService
) {
// TODO: PagedResponse<Task>としたい
    fun getTasks(userId: Long, filter: TaskFilter, pageable: Pageable): PagedResponse =
        userService.getUserById(userId)
            .let{user -> taskRepository.findAllWithFilter(user.id, filter.title, filter.status, pageable)}
            .let { page -> PagedResponse(
                content = page.content.map { it.toDomain() },
                totalElements = page.totalElements,
                totalPages = page.totalPages,
                currentPage = page.number,
                hasNext = page.hasNext(),
                hasPrevious =  page.hasPrevious()
            ) }

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

    fun deleteTask(userId: Long, taskId: Long) {
        userService.getUserById(userId)
        val found = taskRepository.findById(taskId)
            .orElseThrow{ TaskNotFoundException("タスクが存在しません") }
            .toDomain()
        if(found.userId != userId) throw AccessDeniedException("アクセス権限がありません")

        taskRepository.delete(found.toEntity())
    }


}