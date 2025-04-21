package ru.projektio.boardservice.database.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import ru.projektio.boardservice.database.entity.BoardEntity

interface BoardDao: CrudRepository<BoardEntity, Long> {
    fun findBoardEntityByBoardNameContainingIgnoreCase(boardName: String, pageable: Pageable): MutableList<BoardEntity>
    fun findBoardEntityByBoardNameContainingIgnoreCase(boardName: String): MutableList<BoardEntity>
    fun findAll(pageable: Pageable): Page<BoardEntity>
    fun getBoardEntityById(id: Long): MutableList<BoardEntity>
    fun findBoardEntityById(id: Long): MutableList<BoardEntity>
}