package ru.projektio.boardservice.dto.request

data class UpdateBoardRequest(
    val title: String,
    val description: String?,
    val userIds: List<Long>
)
