package com.example.userapi.controller

import com.example.userapi.model.TaskStatus
import com.example.userapi.repository.TaskEntity
import com.example.userapi.repository.TaskRepository
import com.example.userapi.repository.UserEntity
import com.example.userapi.repository.UserRepository
import jakarta.transaction.Transactional
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Suppress("NonAsciiCharacters")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TaskControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var taskRepository: TaskRepository

    private var user1Id: Long = 0
    private var task1Id: Long = 0
    private var task2Id: Long = 0

    @BeforeEach
    fun setUp() {
        val user1 = userRepository.save(UserEntity(name = "Alice", email = "alice@example.com"))
        user1Id = user1.id

        val task1 = taskRepository.save(TaskEntity(userId = user1Id, title = "Shopping", description = "go shopping", status = TaskStatus.TODO))
        task1Id = task1.id
        val task2 = taskRepository.save(TaskEntity(userId = user1Id, title = "Training", description = "go training", status = TaskStatus.TODO))
        task2Id = task2.id
    }

    @Test
    fun `GET getTasks_指定した条件に合致するタスク一覧を返す`() {
        mockMvc.perform(get("/users/$user1Id/tasks?title=ing&status=TODO"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$", hasSize<Any>(2)))
            .andExpect(jsonPath("$[0].title").value("Shopping"))
            .andExpect(jsonPath("$[1].title").value("Training"))
    }

    @Test
    fun `GET getTasks_不正なステータスを指定した場合、400を返す`() {
        mockMvc.perform(get("/users/$user1Id/tasks?title=ing&status=AAAA"))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `GET getTasks_不正なユーザーを指定した場合、404を返す`() {
        val invalidUserId = 99999
        mockMvc.perform(get("/users/$invalidUserId/tasks?title=ing&status=TODO"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun getTaskById() {
    }

    @Test
    fun createTask() {
    }

    @Test
    fun updateTask() {
    }

    @Test
    fun deleteTask() {
    }

}