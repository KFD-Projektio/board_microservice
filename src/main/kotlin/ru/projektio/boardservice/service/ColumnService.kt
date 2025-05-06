package ru.projektio.boardservice.service

import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Service
import ru.projektio.boardservice.database.entity.BoardColumnEntity
import ru.projektio.boardservice.database.entity.ColumnEntity
import ru.projektio.boardservice.database.mapper.ColumnMapper
import ru.projektio.boardservice.database.repository.BoardDao
import ru.projektio.boardservice.database.repository.ColumnDao
import ru.projektio.boardservice.dto.request.CreateColumnRequest
import ru.projektio.boardservice.dto.request.NewColumnTitleRequest
import ru.projektio.boardservice.dto.response.BoardDataResponse
import ru.projektio.boardservice.dto.response.ColumnDataResponse
import ru.projektio.boardservice.exception.NoColumnException
import ru.projektio.boardservice.exception.NoContentException
import ru.projektio.boardservice.exception.RestrictedUserException
import ru.projektio.boardservice.exception.WrongColumnOrderException
import kotlin.math.max
import kotlin.math.min

@Service
class ColumnService(
    private val boardDao: BoardDao,
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
        val board = boardService.getBoardById(userId, boardId)

        val newColumn = ColumnEntity (
            columnTitle = data.title,
            boardId = boardId
        )
        newColumn.columnPosition = board.columnsIds.size
        columnDao.save(newColumn)

        val boardEnt = boardDao.findById(board.id).orElseThrow { NoColumnException("Нет иди нах") }
        boardEnt.boardColumns.add(BoardColumnEntity(
            columnId = newColumn.id,
            boardId = board.id
        ))

        boardDao.save(boardEnt)

        return columnMapper.columnData(newColumn)
    }

    fun changeColumnTitle(userId: Long, boardId: Long, columnPosition: Int, data: NewColumnTitleRequest): ColumnDataResponse {
        try {val boardColumns = boardService.getBoardById(userId, boardId).columnsIds
            if (columnPosition > boardColumns.size || columnPosition < 0) {
                throw NoColumnException("The position is wrong and/or the board doesn't have that many columns")
            }
            val columnId = boardColumns[columnPosition]
            val column = getColumnByIdInternal(columnId)
            column.columnTitle = data.title
            columnDao.save(column)
            return columnMapper.columnData(column)
        }
        catch (e: RestrictedUserException) {throw RestrictedUserException("Can't change columns on board that you can't see")}
    }

    @Transactional
    fun reorderColumns(userId: Long, boardId: Long, newOrder: IntArray) {
        try {boardService.getBoardById(userId, boardId)} catch (e: RestrictedUserException) {throw RestrictedUserException("Can't reorder columns on this board")}
        val columns = columnDao.getColumnEntityByBoardId(boardId)
        columns.sortBy { it.columnPosition }
        if (newOrder.size != columns.size) { throw WrongColumnOrderException("New positions aren't listed for each column")}
        for (i in 0..<columns.size) {
            if (i !in newOrder) { throw WrongColumnOrderException("New positions can't have skips or be less than zero.") }
            columns[i].columnPosition = newOrder[i]
            columnDao.save(columns[i])
        }
    }

    @Transactional
    fun swapColumns(userId: Long, boardId: Long, position1: Int, position2: Int) {
        boardService.getBoardById(userId, boardId)
        val columns = columnDao.getColumnEntityByBoardId(boardId)
        columns.sortBy { it.columnPosition }
        if (columns.isNullOrEmpty()) { throw NoColumnException("There are no columns in the board") }
        if (columns.size < max(position1, position2) || min(position1, position2) < 0) { throw NoColumnException("There are no such columns in the board") }

        columns[position1].columnPosition = position2
        columns[position2].columnPosition = position1
        columnDao.save(columns[position1])
        columnDao.save(columns[position2])
    }

    fun deleteColumn(userId: Long, boardId: Long, columnPosition: Int) {
        try {
            boardService.getBoardById(userId, boardId)
        } catch (e: RestrictedUserException) {
            throw RestrictedUserException("Can't delete columns on this board")
        }

        val board = boardDao.findById(boardId).orElseThrow { NoColumnException("Доска не найдена") }

        val columnToDelete = columnDao.findByBoardIdAndColumnPosition(boardId, columnPosition)
            ?: throw NoContentException("Колонка не найдена")

        board.boardColumns.removeIf { it.columnId == columnToDelete.id }
        columnDao.delete(columnToDelete)
        columnDao.findAllByBoardIdOrderByColumnPositionAsc(boardId)
            .forEachIndexed { index, column ->
                column.columnPosition = index
                columnDao.save(column)
            }
        boardDao.save(board)
    }

    fun getColumnInfoByIdInternal(columnId: Long): ColumnDataResponse {
        return columnDao.findColumnEntityById(columnId).map { columnMapper.columnData(it) }[0]

    }
}
