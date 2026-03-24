package com.example.userapi.repository

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@Suppress("NonAsciiCharacters")
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() {
        userRepository.save(UserEntity(name = "Alice", email = "alice@example.com"))
    }

    @Test
    fun `findByEmail_存在するEmailの場合、ユーザーを返す`() {
        val actual = userRepository.findByEmail("alice@example.com")

        assertEquals("Alice", actual?.name)
        assertEquals("alice@example.com", actual?.email)
    }

    @Test
    fun `findByEmail_存在しないEmailの場合、nullを返す`() {
        val actual = userRepository.findByEmail("bob@example.com")

        assertNull(actual)
    }

}