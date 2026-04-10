package com.example.userapi.dto

import com.example.userapi.model.TaskStatus

data class UpdateTaskRequest(val title: String?, val description: String?, val status: TaskStatus?)