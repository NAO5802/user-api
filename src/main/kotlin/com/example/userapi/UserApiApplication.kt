package com.example.userapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

fun main(args: Array<String>) {
    runApplication<UserApiApplication>(*args)
}

@SpringBootApplication
@EnableJpaAuditing
class UserApiApplication
