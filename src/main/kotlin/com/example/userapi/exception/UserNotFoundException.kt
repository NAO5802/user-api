package com.example.userapi.exception

class UserNotFoundException(override val message: String?) : RuntimeException(message)