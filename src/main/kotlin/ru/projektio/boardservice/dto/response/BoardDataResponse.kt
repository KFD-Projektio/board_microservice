package ru.projektio.boardservice.dto.response

data class BoardDataResponse(
    val boardName: String,
    val boardDescription: String?,
    val columnsIds: List<Long>,
    val userIds: List<Long>
)
