package com.example.kotlinstudy.todo

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
data class Task(
    @Id @GeneratedValue val id: Long,
    val description: String
)
