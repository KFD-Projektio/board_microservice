package ru.projektio.boardservice.dto.request

data class CreateColumnRequest(
    val title: String,
    val position: Int
)
