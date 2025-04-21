package ru.projektio.boardservice.dto.response

data class ExceptionResponse(
    val cause: Throwable?,
    val message: String?
)
