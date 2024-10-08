package com.example.kotlinstudy.todo

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/task")
class TaskController(val service: TaskService) {

    @GetMapping
    fun tasks() = ResponseEntity.ok(service.getAll())

    @PostMapping
    fun create(@RequestBody task: Task): ResponseEntity<String> {
        val result = service.save(task)

        return ResponseEntity.ok(
            "added task with description ${result.description}"
        )
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) = if (service.delete(id)) {
        ResponseEntity.ok("Task with id $id deleted")
    } else {
        ResponseEntity.status(404).body("Task with id $id not found")
    }

}          
