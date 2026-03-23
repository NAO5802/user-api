package com.example.userapi.repository

import com.example.userapi.model.TaskStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface TaskRepository: JpaRepository<TaskEntity, Long>{
    @Query("""
        SELECT t FROM TaskEntity t 
        WHERE t.userId = :userId
        AND (:title IS NULL OR t.title LIKE %:title%)
        AND (:status IS NULL OR t.status = : status)
    """)
    fun findAllWithFilter(@Param("userId") userId: Long, @Param("title") title: String?,@Param("status") status: TaskStatus?): List<TaskEntity>
}