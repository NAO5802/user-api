package com.example.userapi.service

import com.example.userapi.exception.UserNotFoundException
import com.example.userapi.model.CreateUserRequest
import com.example.userapi.model.User
import com.example.userapi.repository.UserEntity
import com.example.userapi.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.Optional

@Suppress("NonAsciiCharacters")
class UserServiceTest {

    private val userRepository: UserRepository = mock()
    private val userService = UserService(userRepository)

    private val entityOfUser1 = UserEntity(1L, "Alice", "alice@example.com")
    private val entityOfUser2 = UserEntity(2L, "Bob", "bob@example.com")
    private val entityOfUser3 = UserEntity(3L, "Cathy", "cathy@example.com")
    private val user1 = User(1L, "Alice", "alice@example.com")
    private val user2 = User(2L, "Bob", "bob@example.com")
    private val newUserRequest = CreateUserRequest("Cathy", "cathy@example.com")
    private val existingUserRequest = CreateUserRequest("Alice", "alice@example.com")

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
    fun `createUser_正しいリクエストの場合、作成したユーザーを返す`() {
        whenever(userRepository.findByEmail(newUserRequest.email)).thenReturn(null)
        // NOTE: Mockioは引数をequalsで照合する。class(非data class)のequalsは参照比較のため別インスタンスはマッチしない。テストではany()で回避している
        whenever(userRepository.save(any())).thenReturn(entityOfUser3)

        val actual = userService.createUser(newUserRequest)

        assertEquals("Cathy", actual.name)
        assertEquals("cathy@example.com", actual.email)
    }

    @Test
    fun `createUser_重複したEmalをリクエストした場合、例外を返す`() {
        whenever(userRepository.findByEmail(existingUserRequest.email)).thenReturn(entityOfUser1)

        assertThrows<IllegalArgumentException> { userService.createUser(existingUserRequest) }
    }

}