package ru.projektio.boardservice.dto.request

data class CreateBoardRequest(
    val title: String,
    val description: String?,
    val ownerId: Long,
    val isPrivate: Boolean?
)
