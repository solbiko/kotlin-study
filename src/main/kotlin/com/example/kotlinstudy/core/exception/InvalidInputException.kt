package com.example.kotlinstudy.core.exception

class InvalidInputException (
    message : String = "Invalid Input"
) : RuntimeException(message)