package ru.projektio.boardservice.database.repository

import org.springframework.data.repository.CrudRepository
import ru.projektio.boardservice.database.entity.ColumnEntity

interface ColumnDao : CrudRepository<ColumnEntity, Long> {
    fun getColumnEntityByBoardId(boardId: Long): MutableList<ColumnEntity>
    fun deleteColumnEntityByBoardIdAndColumnPosition(boardId: Long, columnPosition: Int)
    fun getColumnEntityById(id: Long): MutableList<ColumnEntity>
    fun findByBoardIdAndColumnPosition(boardId: Long, position: Int): ColumnEntity?
    fun findAllByBoardIdOrderByColumnPositionAsc(boardId: Long): List<ColumnEntity>
    fun findColumnEntityById(id: Long): MutableList<ColumnEntity>
}