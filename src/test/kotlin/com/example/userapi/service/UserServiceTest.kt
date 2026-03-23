package com.example.userapi.service

import com.example.userapi.exception.UserNotFoundException
import com.example.userapi.model.User
import com.example.userapi.repository.UserEntity
import com.example.userapi.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.Optional

class UserServiceTest {

    private val userRepository: UserRepository = mock()
    private val userService = UserService(userRepository)

    private val userEntity = UserEntity(1L, "Alice", "alice@example.com")

    @Test
    fun getAllUsers() {
    }

    @Test
    fun `getUserById_ユーザーが存在する時、ユーザーを返す`() {
        whenever(userRepository.findById(1L)).thenReturn(Optional.of(userEntity))

        val actual = userService.getUserById(1L)

        val expected = User(1L, "Alice", "alice@example.com")
        assertEquals(expected, actual)
    }

    @Test
    fun `getUserById_ユーザーが存在しない時、例外を返す`() {
        whenever(userRepository.findById(999L)).thenReturn(Optional.empty())

        assertThrows<UserNotFoundException> { userService.getUserById(999L) }
    }

    @Test
    fun createUser() {
    }

}