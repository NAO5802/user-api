package com.example.userapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

fun main(args: Array<String>) {
    runApplication<UserApiApplication>(*args)
}

@SpringBootApplication
class UserApiApplication
