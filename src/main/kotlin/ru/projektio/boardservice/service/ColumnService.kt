package ru.projektio.boardservice.service

import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Service
import ru.projektio.boardservice.database.entity.ColumnEntity
import ru.projektio.boardservice.database.mapper.ColumnMapper
import ru.projektio.boardservice.database.repository.ColumnDao
import ru.projektio.boardservice.dto.request.CreateColumnRequest
import ru.projektio.boardservice.dto.request.NewColumnTitleRequest
import ru.projektio.boardservice.dto.response.ColumnDataResponse
import ru.projektio.boardservice.exception.NoColumnException
import ru.projektio.boardservice.exception.WrongColumnOrderException

@Service
class ColumnService(
    private val columnDao: ColumnDao,
    private val columnMapper: ColumnMapper
) {

    fun getBoardColumns(boardId: Long): List<ColumnEntity> {
        return columnDao.getColumnEntityByBoardId(boardId).toList()
    }

    @Transactional
    fun addColumn(boardId: Long, data: CreateColumnRequest): ColumnDataResponse {
        val newColumn = ColumnEntity (
            columnTitle = data.title,
            boardId = boardId
        )
        newColumn.columnPosition = data.position
        columnDao.save(newColumn)
        return columnMapper.columnData(newColumn)
    }

    fun changeColumnTitle(boardId: Long, columnPosition: Int, data: NewColumnTitleRequest): ColumnDataResponse {
        val boardColumns = getBoardColumns(boardId)
        if (columnPosition > boardColumns.size || columnPosition < 0) {
            throw NoColumnException("The postiton is wrong and/or the board doesn't have that many columns")
        }
        val column = boardColumns[columnPosition]
        column.columnTitle = data.title
        columnDao.save(column)
        return columnMapper.columnData(column);
    }

    fun reorderColumns(boardId: Long, newOrder: IntArray) {
        val columns = columnDao.getColumnEntityByBoardId(boardId)
        if (newOrder.size != columns.size) { throw WrongColumnOrderException("New positions aren't listed for each column")}
        for (i in 0..<columns.size) { columns[i].columnPosition = newOrder[i]; columnDao.save(columns[i])}
    }

    fun deleteColumn(boardId: Long, columnPosition: Int) {
        columnDao.deleteColumnEntityByBoardIdAndColumnPosition(boardId, columnPosition)
    }
}