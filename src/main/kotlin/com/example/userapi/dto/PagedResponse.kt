package com.example.userapi.dto

import com.example.userapi.model.Task

data class PagedResponse(
    val content: List<Task>,
    val totalElements: Int,
    val totalPages: Int,
    val currentPage: Int,
    val hasNext: Boolean,
    val hasPrevious: Boolean
    )