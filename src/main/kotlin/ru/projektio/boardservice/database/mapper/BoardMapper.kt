package ru.projektio.boardservice.database.mapper

import org.springframework.stereotype.Component
import ru.projektio.boardservice.database.entity.BoardEntity
import ru.projektio.boardservice.dto.response.BoardDataResponse
import ru.projektio.boardservice.service.ColumnService

@Component
class BoardMapper {
    fun boardData(board: BoardEntity) = BoardDataResponse(
        boardName = board.boardName,
        boardDescription = board.boardDescription,
        columnsIds = board.boardColumns.map { it.columnId },
        userIds = board.boardUsers.map { it.userId }
    )
}
