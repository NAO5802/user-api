package com.example.userapi.util

import com.example.userapi.model.TaskSortKey
import org.springframework.data.domain.Sort

fun sortFromString(sortBy: String, sortDir: String): Sort {
    val key = TaskSortKey.fromString(sortBy)
    val direction = Sort.Direction.fromString(sortDir)

    return Sort.by(Sort.Order(direction, key.label))
}