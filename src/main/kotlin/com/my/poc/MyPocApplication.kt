package com.my.poc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MyPocApplication

fun main(args: Array<String>) {
    runApplication<MyPocApplication>(*args)
}
