package com.example.userapi.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@Suppress("NonAsciiCharacters")
class TaskFilterTest {

    @Test
    fun `of_タイトルとステータスを指定して、フィルタを生成できる`(){
        val actual = TaskFilter.of(title = "My task", status = "TODO")

        assertEquals("My task", actual.title)
        assertEquals(TaskStatus.TODO, actual.status)
    }

    @Test
    fun `of_不正なステータスを指定した場合、例外を返す`(){
        assertThrows<IllegalArgumentException> { TaskFilter.of(title = "My task", status = "AAAA") }
    }
}