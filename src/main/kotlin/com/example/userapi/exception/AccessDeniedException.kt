package com.example.userapi.exception

class AccessDeniedException(override val message: String?): RuntimeException(message)