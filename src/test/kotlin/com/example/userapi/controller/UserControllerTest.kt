package com.example.userapi.controller

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Suppress("NonAsciiCharacters")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc
    @Autowired
    lateinit var userRepository: UserRepository

    private var aliceId: Long = 0

    @BeforeEach
    fun setup(){
        val alice =userRepository.save(UserEntity(name = "Alice", email = "alice@example.com"))
        aliceId = alice.id
        userRepository.save(UserEntity(name = "Bob", email = "bob@example.com"))
    }

    @Test
    fun `GET users ユーザー一覧を返す`() {
        mockMvc.perform(get("/users"))
            .andExpect(status().isOk )
            .andExpect(jsonPath("$", hasSize<Any>(2)))
            .andExpect(jsonPath("$[0].name").value("Alice"))
            .andExpect(jsonPath("$[1].name").value("Bob"))
    }

    @Test
    fun `GET users id 指定したIDのユーザーを返す`() {
        mockMvc.perform(get("/users/$aliceId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Alice"))
    }

    @Test
    fun `GET users id 指定したIDのユーザーが存在しない場合、404を返す`() {
        mockMvc.perform(get("/users/999999"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `POST users 作成したユーザーを返す`() {
        mockMvc.perform(
            post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name":"Cathy","email":"cathy@example.com"}""")
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.name").value("Cathy"))
    }

    @Test
    fun `POST users 登録済みのEmailでユーザー作成しようとした場合、400を返す`() {
        mockMvc.perform(
            post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"name":"Alice2","email":"alice@example.com"}""")
        )
            .andExpect(status().isBadRequest)
    }

}