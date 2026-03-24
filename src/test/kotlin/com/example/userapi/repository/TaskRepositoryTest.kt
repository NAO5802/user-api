package com.example.userapi.repository

import com.example.userapi.model.TaskStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@Suppress("NonAsciiCharacters")
@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    lateinit var repository: TaskRepository

    private val user1Task1 = TaskEntity(userId = 1L, title = "Alice's task 1", description = "description", status = TaskStatus.TODO)
    private val user1Task2 = TaskEntity(userId = 1L, title = "Alice's task 2", description = "description", status = TaskStatus.DONE)
    private val user1Task3 = TaskEntity(userId = 1L, title = "Alice's task 10", description = "description", status = TaskStatus.DONE)
    private val user2Task1 = TaskEntity(userId = 2L, title = "Bob's task 1", description = "description", status = TaskStatus.TODO)

    @BeforeEach
    fun setUp() {
        repository.save(user1Task1)
        repository.save(user1Task2)
        repository.save(user1Task3)
        repository.save(user2Task1)
    }

    @Test
    fun `findAllWithFilter_フィルタを指定しない場合、対象ユーザーのタスクを全て取得する`() {
        val actual = repository.findAllWithFilter(userId = 1L , title = null, status = null)

        assertEquals(3, actual.size)
        assertTask(user1Task1, actual[0])
        assertTask(user1Task2, actual[1])
        assertTask(user1Task3, actual[2])
    }

    @Test
    fun `findAllWithFilter_タイトルを指定した場合、対象ユーザーのタスクのうちタイトルが部分一致するものを全て取得する`() {
        val actual = repository.findAllWithFilter(userId = 1L, title = "Alice's task 1", status = null)

        assertEquals(2, actual.size)
        assertTask(user1Task1, actual[0])
        assertTask(user1Task3, actual[1])

    }

    @Test
    fun `findAllWithFilter_ステータスを指定した場合、対象ユーザーのタスクのうちステータスが一致するものを全て取得する`() {
        val actual = repository.findAllWithFilter(userId = 1L, title = null, status = TaskStatus.DONE)

        assertEquals(2, actual.size)
        assertTask(user1Task2, actual[0])
        assertTask(user1Task3, actual[1])
    }

    @Test
    fun `findAllWithFilter_タイトルとステータスを指定した場合、対象ユーザーのタスクのうち両方の条件に一致するものを全て取得する`() {
        val actual = repository.findAllWithFilter(userId = 1L, title = "Alice's task 1", status = TaskStatus.DONE)

        assertEquals(1, actual.size)
        assertTask(user1Task3, actual[0])
    }

    private fun assertTask(expected: TaskEntity, actual: TaskEntity){
        assertEquals(expected.id, actual.id)
        assertEquals(expected.title, actual.title)
        assertEquals(expected.description, actual.description)
        assertEquals(expected.status, actual.status)
    }
}
