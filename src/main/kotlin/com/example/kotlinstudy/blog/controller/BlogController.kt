package com.example.kotlinstudy.blog.controller

import com.example.kotlinstudy.blog.dto.BlogDto
import com.example.kotlinstudy.blog.service.BlogService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/blog")
@RestController
class BlogController(
    val blogService:BlogService
){
    @GetMapping("")
    fun search(@RequestBody blogDto: BlogDto): String? {
        val result = blogService.serchKakao(blogDto)
        return result
    }
}