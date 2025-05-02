package ru.projektio.boardservice.dto.response

data class BoardDataResponse (
    val id: Long,
    val boardName: String,
    val boardDescription: String?,
    val columnsIds: List<Long>,
    val userIds: List<Long>
)
