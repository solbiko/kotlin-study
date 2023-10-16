package com.example.kotlinstudy.blog.repository

import com.example.kotlinstudy.blog.entiry.Wordcount
import org.springframework.data.repository.CrudRepository


interface WordRepository : CrudRepository<Wordcount, String>