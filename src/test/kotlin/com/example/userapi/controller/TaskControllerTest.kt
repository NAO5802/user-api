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
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
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
    private var user2Id: Long = 0
    private var task1Id: Long = 0
    private var task2Id: Long = 0
    private var user2Task1Id: Long = 0

    @BeforeEach
    fun setUp() {
        val user1 = userRepository.save(UserEntity(name = "Alice", email = "alice@example.com"))
        user1Id = user1.id
        val user2 = userRepository.save(UserEntity(name = "Bob", email = "bob@example.com"))
        user2Id = user2.id

        val task1 = taskRepository.save(TaskEntity(userId = user1Id, title = "Shopping", description = "go shopping", status = TaskStatus.TODO))
        task1Id = task1.id
        val task2 = taskRepository.save(TaskEntity(userId = user1Id, title = "Training", description = "go training", status = TaskStatus.TODO))
        task2Id = task2.id
        val user2Task1 = taskRepository.save(TaskEntity(userId = user2Id, title = "Bob's task", description = "bob's task", status = TaskStatus.TODO))
        user2Task1Id = user2Task1.id
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
    fun `GET getTaskById_指定したIDのタスクを返す`() {
        mockMvc.perform(get("/users/$user1Id/tasks/$task1Id"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value("Shopping"))
    }

    @Test
    fun `GET getTaskById_不正なユーザーを指定した場合、404を返す`() {
        mockMvc.perform(get("/users/99999/tasks/$task1Id"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `GET getTaskById_指定したIDのタスクが存在しない場合、404を返す`() {
        mockMvc.perform(get("/users/$user1Id/tasks/99999"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `GET getTaskById_指定したユーザーのIDがタスクのものと一致しない場合、403を返す`() {
        mockMvc.perform(get("/users/$user1Id/tasks/$user2Task1Id"))
            .andExpect(status().isForbidden)
    }


    @Test
    fun `POST createTask_作成したタスクを返す`() {
        mockMvc.perform(
            post("/users/$user1Id/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"title":"New Task","description":"new task description"}""")
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.title").value("New Task"))
            .andExpect(jsonPath("$.status").value("TODO"))
    }

    @Test
    fun `POST createTask_不正なユーザーを指定した場合、404を返す`() {
        mockMvc.perform(
            post("/users/99999/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"title":"New Task","description":"new task description"}""")
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `PUT updateTask_更新したタスクを返す`() {
        mockMvc.perform(
            put("/users/$user1Id/tasks/$task1Id")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"title":"Updated","status":"DONE"}""")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value("Updated"))
            .andExpect(jsonPath("$.status").value("DONE"))
    }

    @Test
    fun `PUT updateTask_不正なユーザーを指定した場合、404を返す`() {
        mockMvc.perform(
            put("/users/99999/tasks/$task1Id")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"title":"Updated"}""")
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `PUT updateTask_指定したIDのタスクが存在しない場合、404を返す`() {
        mockMvc.perform(
            put("/users/$user1Id/tasks/99999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"title":"Updated"}""")
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `PUT updateTask_指定したユーザーのIDがタスクのものと一致しない場合、403を返す`() {
        mockMvc.perform(
            put("/users/$user1Id/tasks/$user2Task1Id")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"title":"Updated"}""")
        )
            .andExpect(status().isForbidden)
    }

    @Test
    fun `DELETE deleteTask_タスクの削除に成功したら204を返す`() {
        mockMvc.perform(delete("/users/$user1Id/tasks/$task1Id"))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `DELETE deleteTask_不正なユーザーを指定した場合、404を返す`() {
        mockMvc.perform(delete("/users/99999/tasks/$task1Id"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `DELETE deleteTask_指定したIDのタスクが存在しない場合、404を返す`() {
        mockMvc.perform(delete("/users/$user1Id/tasks/99999"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `DELETE deleteTask_指定したユーザーのIDがタスクのものと一致しない場合、403を返す`() {
        mockMvc.perform(delete("/users/$user1Id/tasks/$user2Task1Id"))
            .andExpect(status().isForbidden)
    }

}