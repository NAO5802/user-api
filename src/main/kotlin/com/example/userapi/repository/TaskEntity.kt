package com.example.userapi.repository

import com.example.userapi.model.Task
import com.example.userapi.model.TaskStatus
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "tasks")
@EntityListeners(AuditingEntityListener::class)
class TaskEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val userId: Long,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val description: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val status: TaskStatus,

    @Column(nullable = false, updatable = false)
    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now(),
)

fun TaskEntity.toDomain(): Task =
    Task(
        id = this.id,
        userId = this.userId,
        title = this.title,
        description = this.description,
        status = this.status,
        createdAt = this.createdAt
    )