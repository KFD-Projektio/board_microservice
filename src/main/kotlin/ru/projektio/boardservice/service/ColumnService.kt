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
import ru.projektio.boardservice.exception.RestrictedUserException
import ru.projektio.boardservice.exception.WrongColumnOrderException

@Service
class ColumnService(
    private val columnDao: ColumnDao,
    private val columnMapper: ColumnMapper,
    private val boardService: BoardService,
) {

    fun getBoardColumns(userId: Long, boardId: Long): List<ColumnDataResponse> {
        try { boardService.getBoardById(userId, boardId)
            return columnDao.getColumnEntityByBoardId(boardId).toList().map {columnMapper.columnData(it)}
        }
        catch (e: RestrictedUserException) {
            throw RestrictedUserException("Can't check columns on board that you can't see")
        }
    }

    fun getColumnByIdInternal(columnId: Long): ColumnEntity {
        return columnDao.getColumnEntityById(columnId)[0]
    }

    @Transactional
    fun addColumn(userId: Long, boardId: Long, data: CreateColumnRequest): ColumnDataResponse {
        try { boardService.getBoardById(userId, boardId) }
        catch (e: RestrictedUserException) {
            throw RestrictedUserException("Can't add columns on board that you can't see")
        }

        val newColumn = ColumnEntity (
            columnTitle = data.title,
            boardId = boardId
        )
        newColumn.columnPosition = data.position
        columnDao.save(newColumn)
        return columnMapper.columnData(newColumn)
    }

    fun changeColumnTitle(userId: Long, boardId: Long, columnPosition: Int, data: NewColumnTitleRequest): ColumnDataResponse {
        try {val boardColumns = boardService.getBoardById(userId, boardId).columnsIds
            if (columnPosition > boardColumns.size || columnPosition < 0) {
                throw NoColumnException("The postiton is wrong and/or the board doesn't have that many columns")
            }
            val columnId = boardColumns[columnPosition]
            val column = getColumnByIdInternal(columnId)
            column.columnTitle = data.title
            columnDao.save(column)
            return columnMapper.columnData(column)
        }
        catch (e: RestrictedUserException) {throw RestrictedUserException("Can't change columns on board that you can't see")}
    }

    fun reorderColumns(userId: Long, boardId: Long, newOrder: IntArray) {
        try {boardService.getBoardById(userId, boardId)} catch (e: RestrictedUserException) {throw RestrictedUserException("Can't reorder columns on board that you can't see")}
        val columns = columnDao.getColumnEntityByBoardId(boardId)
        if (newOrder.size != columns.size) { throw WrongColumnOrderException("New positions aren't listed for each column")}
        for (i in 0..<columns.size) { columns[i].columnPosition = newOrder[i]; columnDao.save(columns[i])}
    }

    fun deleteColumn(userId: Long, boardId: Long, columnPosition: Int) {
        try {boardService.getBoardById(userId, boardId)} catch (e: RestrictedUserException) {throw RestrictedUserException("Can't delete columns on board that you can't see")}
        columnDao.deleteColumnEntityByBoardIdAndColumnPosition(boardId, columnPosition)
    }
}
