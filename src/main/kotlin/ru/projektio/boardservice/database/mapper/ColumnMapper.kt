package ru.projektio.boardservice.database.mapper

import org.springframework.stereotype.Component
import ru.projektio.boardservice.database.entity.ColumnEntity
import ru.projektio.boardservice.dto.response.ColumnDataResponse

@Component
class ColumnMapper {
    fun columnData(column: ColumnEntity): ColumnDataResponse {
        return ColumnDataResponse(
            id = column.id,
            title = column.columnTitle,
            boardId = column.boardId,
            columnPosition = column.columnPosition,
            createdAt = column.createdAt,
            updatedAt = column.updatedAt
        )
    }
}