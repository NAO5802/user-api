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

    private val entityOfUser1 = UserEntity(1L, "Alice", "alice@example.com")
    private val entityOfUser2 = UserEntity(2L, "Bob", "bob@example.com")
    private val user1 = User(1L, "Alice", "alice@example.com")
    private val user2 = User(2L, "Bob", "bob@example.com")

    @Test
    fun `getAllUsers_ユーザーが存在する時、全てのユーザーを返す`() {
        whenever(userRepository.findAll()).thenReturn(listOf(entityOfUser1, entityOfUser2))

        val actual = userService.getAllUsers()

        assertEquals(listOf(user1, user2), actual)
    }

    @Test
    fun `getAllUsers_ユーザーが存在しない時、空配列を返す`() {
        whenever(userRepository.findAll()).thenReturn(emptyList<UserEntity>())

        val actual = userService.getAllUsers()

        assertEquals(emptyList<User>(), actual)

    }

    @Test
    fun `getUserById_ユーザーが存在する時、ユーザーを返す`() {
        whenever(userRepository.findById(1L)).thenReturn(Optional.of(entityOfUser1))

        val actual = userService.getUserById(1L)

        assertEquals(user1, actual)
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