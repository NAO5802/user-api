package com.example.userapi.util

import com.example.userapi.model.TaskSortKey
import org.springframework.data.domain.Sort

fun sortFromString(sortBy: String, sortDir: String): Sort {
    val sortBy = sortBy.let { TaskSortKey.fromString(it) }
    val sortDir = sortDir.let{ Sort.Direction.fromString(it) }

    return Sort.by(Sort.Order(sortDir, sortBy.label))
}