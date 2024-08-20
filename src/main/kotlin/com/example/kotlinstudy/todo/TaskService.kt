package com.example.kotlinstudy.todo

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TaskService(val repository: TaskRepository) {
    fun getAll(): List<Task> = repository.findAll()

    @Transactional
    fun save(task: Task) = repository.save(task)

    fun delete(id: Long): Boolean {
        val found = repository.existsById(id)

        if (found) {
            repository.deleteById(id)
        }

        return found
    }
}
