package ru.projektio.boardservice.database.mapper

import org.springframework.stereotype.Component
import ru.projektio.boardservice.database.entity.BoardEntity
import ru.projektio.boardservice.dto.response.BoardDataResponse
import ru.projektio.boardservice.service.ColumnService

@Component
class BoardMapper (
    private val columnService: ColumnService
) {
    fun boardData(board: BoardEntity) = BoardDataResponse(
        boardName = board.boardName,
        boardDescription = board.boardDescription,
        columnsIds = columnService.getBoardColumns(board.id).map{it.id},
        userIds = board.userIDs
    )
}