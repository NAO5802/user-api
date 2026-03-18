package com.example.userapi.exception

class TaskNotFoundException(override val message: String?) : RuntimeException(message)