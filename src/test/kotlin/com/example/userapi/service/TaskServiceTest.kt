package com.example.userapi.service

import com.example.userapi.exception.AccessDeniedException
import com.example.userapi.exception.TaskNotFoundException
import com.example.userapi.model.CreateTaskRequest
import com.example.userapi.model.Task
import com.example.userapi.model.TaskFilter
import com.example.userapi.model.TaskStatus
import com.example.userapi.model.UpdateTaskRequest
import com.example.userapi.model.User
import com.example.userapi.repository.TaskEntity
import com.example.userapi.repository.TaskRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import java.util.*

@Suppress("NonAsciiCharacters")
class TaskServiceTest {

    private val taskRepository : TaskRepository = mock()
    private val userService: UserService = mock()
    private val taskService = TaskService(taskRepository, userService)

    val user1 = User(1L, "Alice", "alice@example.com")

    val taskEntity1 = TaskEntity(id = 10L, userId = 1L, title = "Shopping", description = "go shopping", status = TaskStatus.TODO, createdAt = LocalDateTime.of(2026,3,25,11,30))
    val updatedTaskEntity1 = TaskEntity(id = 10L, userId = 1L, title = "My Shopping", description = "let's go my Shopping",  status = TaskStatus.DONE, createdAt = LocalDateTime.of(2026,3,25,11,30))
    val taskEntity2 = TaskEntity(id = 11L, userId = 1L, title = "Shopping2", description = "go shopping2", status = TaskStatus.TODO, createdAt = LocalDateTime.of(2026,3,25,11,35))
    val taskEntity3 = TaskEntity(id = 12L, userId = 2L, title = "Training", description = "go training", status = TaskStatus.TODO, createdAt = LocalDateTime.of(2026,3,25,11,40))
    val taskEntity4 = TaskEntity(id = 13L, userId = 1L, title = "New task", description = "it's new", status = TaskStatus.TODO, createdAt = LocalDateTime.of(2026,3,25,11,45))

    val task1 = Task(id = 10L, userId = 1L, title = "Shopping", description = "go shopping", status = TaskStatus.TODO, createdAt = LocalDateTime.of(2026,3,25,11,30))
    val updatedTask1 = Task(id = 10L, userId = 1L, title = "My Shopping", description = "let's go my Shopping",  status = TaskStatus.DONE, createdAt = LocalDateTime.of(2026,3,25,11,30))
    val task2 = Task(id = 11L, userId = 1L, title = "Shopping2", description = "go shopping2", status = TaskStatus.TODO, createdAt = LocalDateTime.of(2026,3,25,11,35))
    val task4 = Task(id = 13L, userId = 1L, title = "New task", description = "it's new", status = TaskStatus.TODO, createdAt = LocalDateTime.of(2026,3,25,11,45))

    @Test
    fun `getTasks 指定したユーザーとフィルタに合致するすべてのタスクを返す`() {
        whenever(userService.getUserById(1L)).thenReturn(user1)
        whenever(taskRepository.findAllWithFilter(1L, "Shopping", TaskStatus.TODO))
            .thenReturn(listOf(taskEntity1,taskEntity2))

        val actual = taskService.getTasks(1L, TaskFilter.of("Shopping", "TODO"))

        assertEquals(listOf(task1, task2), actual)
    }

    @Test
    fun `getTaskById 指定したIDのタスクを返す`() {
        whenever(userService.getUserById(1L)).thenReturn(user1)
        whenever(taskRepository.findById(10L)).thenReturn(Optional.of(taskEntity1))

        val actual = taskService.getTaskById(1L, 10L)

        assertEquals(task1, actual)
    }

    @Test
    fun `getTaskById 指定したIDのタスクが存在しない場合、例外を返す`() {
        whenever(userService.getUserById(1L)).thenReturn(user1)
        whenever(taskRepository.findById(999L)).thenReturn(Optional.empty())

        assertThrows<TaskNotFoundException> { taskService.getTaskById(1L, 999L)}
    }

    @Test
    fun `getTaskById 指定したユーザーIDがタスクのものと合致しない場合、例外を返す`() {
        whenever(userService.getUserById(1L)).thenReturn(user1)
        whenever(taskRepository.findById(12L)).thenReturn(Optional.of(taskEntity3))

        assertThrows<AccessDeniedException>{taskService.getTaskById(1L, 12L)}
    }

    @Test
    fun `createTask 作成したタスクを返す`() {
        whenever(userService.getUserById(1L)).thenReturn(user1)
        whenever(taskRepository.save(any())).thenReturn(taskEntity4)

        val actual = taskService.createTask(1L, CreateTaskRequest("New task", "it's new"))

        assertEquals(task4, actual)
    }

    @Test
    fun `updateTask 更新したタスクを返す`() {
        whenever(userService.getUserById(1L)).thenReturn(user1)
        whenever(taskRepository.findById(10L)).thenReturn(Optional.of(taskEntity1))
        whenever(taskRepository.save(any())).thenReturn(updatedTaskEntity1)

        val actual = taskService.updateTask(1L, 10L, UpdateTaskRequest(title = "My Shopping", description = "let's go my Shopping",  status = TaskStatus.DONE))

        assertEquals(updatedTask1, actual)
    }

    @Test
    fun `updateTask 指定したIDのタスクが存在しない場合、例外を返す`() {
        whenever(userService.getUserById(1L)).thenReturn(user1)
        whenever(taskRepository.findById(999L)).thenReturn(Optional.empty())

        assertThrows<TaskNotFoundException> { taskService.updateTask(1L, 999L, UpdateTaskRequest("New task", "it's new", TaskStatus.DONE)) }
    }

    @Test
    fun `updateTask 指定したユーザーIDがタスクのものと合致しない場合、例外を返す`() {
        whenever(userService.getUserById(1L)).thenReturn(user1)
        whenever(taskRepository.findById(12L)).thenReturn(Optional.of(taskEntity3))

        assertThrows<AccessDeniedException>{taskService.updateTask(1L, 12L, UpdateTaskRequest("New task", "it's new", TaskStatus.DONE))}
    }

    @Test
    fun `deleteTask 削除に成功した場合、何も返さない`() {
        whenever(userService.getUserById(1L)).thenReturn(user1)
        whenever(taskRepository.findById(10L)).thenReturn(Optional.of(taskEntity1))
        doNothing().whenever(taskRepository).delete(any())

        taskService.deleteTask(1L, 10L)

        verify(taskRepository, times(1)).delete(any())
    }

    @Test
    fun `deleteTask 指定したIDのタスクが存在しない場合、例外を返す`() {
        whenever(userService.getUserById(1L)).thenReturn(user1)
        whenever(taskRepository.findById(999L)).thenReturn(Optional.empty())

        assertThrows<TaskNotFoundException> { taskService.deleteTask(1L, 999L) }
    }

    @Test
    fun `deleteTask 指定したユーザーIDがタスクのものと合致しない場合、例外を返す`() {
        whenever(userService.getUserById(1L)).thenReturn(user1)
        whenever(taskRepository.findById(12L)).thenReturn(Optional.of(taskEntity3))

        assertThrows<AccessDeniedException>{taskService.deleteTask(1L, 12L)}

    }
}