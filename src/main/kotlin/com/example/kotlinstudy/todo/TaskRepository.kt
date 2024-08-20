package com.example.kotlinstudy.todo

import org.springframework.data.jpa.repository.JpaRepository

interface TaskRepository : JpaRepository<Task, Long>
